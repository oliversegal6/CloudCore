package com.xyz.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonObject;
import com.mongodb.util.JSON;
import com.xyz.pojo.Concept;
import com.xyz.pojo.DailyBasic;
import com.xyz.pojo.Stock;
import com.xyz.pojo.StockDaily;
import com.xyz.util.Constant;
import com.xyz.util.DateUtil;
import com.xyz.util.HttpclientUtil;

@Component
public class StockMiningService {

	private static final String CONCEPT_DETAIL = "conceptDetail";

	private static final String CONCEPT = "concept";

	private static final String DAILY_BASIC = "dailyBasic";

	private static final String TOP10_FLOATHOLDERS = "top10Floatholders";
	
	private static final String FINA_INDICATOR = "finaIndicator";

	private static final String STOCK_HIST_DATA = "stocksHistData";

	private static final String STOCK_BASIC = "stockBasic";

	private final Logger logger = LoggerFactory.getLogger(getClass());

	public static final String HTTP_API_TUSHARE_PRO = "http://api.tushare.pro";
	public static final String HTTP_API_TOKEN = "5dfbbdf5953c683a061952a4a6c7eae376dc2a892ee3ce5ed4117d64";
	@Autowired
	private MongoService mongoService;

	public void stockBasic() {
		String body = generateQueryBody("stock_basic", "{\"list_stauts\":\"L\"}");
		mongoService.saveBatch(HttpclientUtil.post(body, HTTP_API_TUSHARE_PRO), STOCK_BASIC);
	}

	public void saveDailyBasic(String trade_date) {
		String body = generateQueryBody("daily_basic", "{\"trade_date\":\"" + trade_date + "\"}");
		mongoService.saveBatch(HttpclientUtil.post(body, HTTP_API_TUSHARE_PRO), "dailyBasic");
	}
	
	public void saveConcept() {
		String body = generateQueryBody(CONCEPT, "{}");
		mongoService.saveBatch(HttpclientUtil.post(body, HTTP_API_TUSHARE_PRO), "concept");
	}
	
	public void saveConceptDetail() {
		List<JSONObject> stocksJson = mongoService.findAll(CONCEPT, 0, 0);
		for (JSONObject stockJson : stocksJson) {
			String code = (String)stockJson.get("code");
			String body = generateQueryBody("concept_detail", "{\"id\":\"" + code + "\"}");
			mongoService.saveBatch(HttpclientUtil.post(body, HTTP_API_TUSHARE_PRO), "conceptDetail");
			logger.info("saveConceptDetail for " + code);
		}
	}

	public List<Concept> findConcept(Integer pageIndex, Integer pageSize) {
		
		if(!Constant.CONCEPT_CACHE.isEmpty())
			return Constant.CONCEPT_CACHE;
		
		List<JSONObject> stocksJson = mongoService.findAll(CONCEPT, pageIndex, pageSize);
		List<Concept> cons = new ArrayList<Concept>();
		for (JSONObject stockJson : stocksJson) {
			Concept con = new Concept();
			con.setCode(stockJson.getString("code"));
			con.setName(stockJson.getString("name"));
			cons.add(con);
		}
		
		return cons;
	}
	
	public List<DailyBasic> findDailyBasic(Integer pageIndex, Integer pageSize) {
		List<JSONObject> stocksJson = mongoService.findAll(DAILY_BASIC, pageIndex, pageSize);
		List<DailyBasic> stocks = new ArrayList<DailyBasic>();
		for (JSONObject stockJson : stocksJson) {
			DailyBasic stock = new DailyBasic();
			stock.setTsCode(stockJson.getString("ts_code"));
			stock.setTradeDate(stockJson.getString("trade_date"));	
			stock.setClose(stockJson.getFloat("close"));
			stock.setTurnoverRate(stockJson.getFloat("turnover_rate"));
			stock.setVolumeRatio(stockJson.getFloat("volume_ratio"));
			stock.setPe(stockJson.getFloat("pe"));
			stock.setPeTtm(stockJson.getFloat("pe_ttm"));
			stock.setPb(stockJson.getFloat("pb"));
			stock.setPs(stockJson.getFloat("ps"));
			stock.setPsTtm(stockJson.getFloat("ps_ttm"));
			stock.setTotalShare(stockJson.getFloat("total_share"));
			stock.setFloatShare(stockJson.getFloat("float_share"));
			stock.setFreeShare(stockJson.getFloat("free_share"));
			stock.setTotalMv(stockJson.getFloat("total_mv"));
			stock.setCircMv(stockJson.getFloat("circ_mv"));
			stocks.add(stock);
		}
		return stocks;
	}
	
	public void saveDataRangeQueryWithCodeAndDate(String tuTable, String dbTable, String code) {
		Calendar cal = Calendar.getInstance();
		String body = genDateRangeQueryBody(tuTable, code, cal);
		List<JsonObject> res = HttpclientUtil.post(body, HTTP_API_TUSHARE_PRO);
		if (res.isEmpty()) {
			logger.info("Frist query is empy. Try again with pre Quarter.");
			cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, -3);
			body = genDateRangeQueryBody(tuTable, code, cal);
			res = HttpclientUtil.post(body, HTTP_API_TUSHARE_PRO);
		}
		mongoService.saveBatch(res, dbTable);
		logger.info("saveStockHistData for " + code + " size: " + res.size());
	}

	public void saveAllTop10Floatholders() {
		List<Stock> allStocks = findAllStocks(0, 0);
		for (Stock stock : allStocks) {
			String code = stock.getTsCode();
			saveDataRangeQueryWithCodeAndDate("top10_floatholders", TOP10_FLOATHOLDERS, code);
		}
	}
	
	public void saveFinaIndicator() {
		List<Stock> allStocks = findAllStocks(0, 0);
		for (Stock stock : allStocks) {
			String code = stock.getTsCode();
			saveDataRangeQueryWithCodeAndDate("fina_indicator", FINA_INDICATOR, code);
		}
	}
	
	private String genDateRangeQueryBody(String tuTable, String code, Calendar cal) {
		Date currentTime = cal.getTime();
		cal.add(Calendar.MONTH, -3);
		Date preTime = cal.getTime();
		String body = generateQueryBody(tuTable, "{\"ts_code\":\"" + code + "\", \"start_date\":\""
				+ DateUtil.dateToStr(preTime) + "\", \"end_date\":\"" + DateUtil.dateToStr(currentTime) + "\"}");
		return body;
	}

	public void saveStockHistDataByCode(String code) {
		String body = generateQueryBody("daily",
				"{\"ts_code\":\"" + code + "\", \"start_date\":\"20160101\", \"end_date\":\"20190104\"}");
		mongoService.saveBatch(HttpclientUtil.post(body, HTTP_API_TUSHARE_PRO), STOCK_HIST_DATA);
	}

	public void saveAllStockHistDataByCode() {
		List<Stock> allStocks = findAllStocks(0, 0);
		for (Stock stock : allStocks) {
			String code = stock.getTsCode();
			logger.info("saveStockHistData for " + code);
			saveStockHistDataByCode(code);
		}
	}

	public List<Stock> findAllStocks(Integer pageIndex, Integer pageSize) {
		if(!Constant.STOCK_CACHE.isEmpty())
			return Constant.STOCK_CACHE ;
		
		List<JSONObject> stocksJson = mongoService.findAll(STOCK_BASIC, pageIndex, pageSize);
		List<Stock> stocks = new ArrayList<Stock>();
		for (JSONObject stockJson : stocksJson) {
			Stock stock = new Stock();
			stock.setTsCode(stockJson.getString("ts_code"));
			stock.setName(stockJson.getString("name"));
			stock.setIndustry(stockJson.getString("industry"));
			stocks.add(stock);
		}
		return stocks;
	}

	public void saveStockHistDataByDate(String date) {
		String body = generateQueryBody("daily", "{\"trade_date\":\"" + date + "\"}");
		mongoService.saveBatch(HttpclientUtil.post(body, HTTP_API_TUSHARE_PRO), STOCK_HIST_DATA);
	}

	public void saveAllStockHistDataByDate(String startDateStr, String endDateStr) {
		Assert.notNull(startDateStr, "start Data can't be null");
		Date startDate = DateUtil.strToDate(startDateStr);
		Date endDate = StringUtils.isBlank(endDateStr) ? Calendar.getInstance().getTime()
				: DateUtil.strToDate(endDateStr);
		Calendar currentCalendar = Calendar.getInstance();
		currentCalendar.setTime(startDate);

		while (currentCalendar.getTime().before(endDate)) {
			String date = DateUtil.dateToStr(currentCalendar.getTime());
			logger.info("saveStockHistData for " + date);
			saveStockHistDataByDate(date);
			currentCalendar.add(Calendar.DAY_OF_MONTH, 1);
		}
	}

	public void saveIndexDailyByDate(String date) {
		String body = generateQueryBody("index_daily",
				"{\"trade_date\":\"" + date + "\", \"start_date\":\"20170101\", \"end_date\":\"20181231\"}");
		mongoService.saveBatch(HttpclientUtil.post(body, HTTP_API_TUSHARE_PRO), STOCK_HIST_DATA);
	}

	public void saveAllIndexDailyByDate(String startDateStr, String endDateStr) {
		Assert.notNull(startDateStr, "start Data can't be null");
		Date startDate = DateUtil.strToDate(startDateStr);
		Date endDate = StringUtils.isBlank(endDateStr) ? Calendar.getInstance().getTime()
				: DateUtil.strToDate(endDateStr);
		Calendar currentCalendar = Calendar.getInstance();
		currentCalendar.setTime(startDate);

		while (currentCalendar.getTime().before(endDate)) {
			String date = DateUtil.dateToStr(currentCalendar.getTime());
			logger.info("saveAllIndexDailyByDate for " + date);
			saveStockHistDataByDate(date);
			currentCalendar.add(Calendar.DAY_OF_MONTH, 1);
		}
	}

	public Map<String, List<JSONObject>> findAllTop10Holders(Integer pageIndex, Integer pageSize) {
		if(!Constant.TOP10_HOLDERS_CACHE.isEmpty())
			return Constant.TOP10_HOLDERS_CACHE;
		
		List<JSONObject> holdersJson = mongoService.findAll(TOP10_FLOATHOLDERS, pageIndex, pageSize);
		Map<String, List<JSONObject>> allHolders = new HashMap<String, List<JSONObject>>();
		for (JSONObject holderJson : holdersJson) {
			String code = holderJson.getString("ts_code");
			if (allHolders.containsKey(code)) {
				allHolders.get(code).add(holderJson);
			} else {
				List<JSONObject> holder = new ArrayList<JSONObject>();
				holder.add(holderJson);
				allHolders.put(code, holder);
			}
		}
		return allHolders;
	}
	
	public Map<String, JSONObject> findAllFinaIndicator(Integer pageIndex, Integer pageSize) {
		if(!Constant.FINA_INDICATOR_CACHE.isEmpty())
			return Constant.FINA_INDICATOR_CACHE;
		
		List<JSONObject> holdersJson = mongoService.findAll(FINA_INDICATOR, pageIndex, pageSize);
		Map<String, JSONObject> allHolders = new HashMap<String, JSONObject>();
		for (JSONObject holderJson : holdersJson) {
			String code = holderJson.getString("ts_code");
			allHolders.put(code, holderJson);
		}
		return allHolders;
	}

	public List<StockDaily> getAllHoldersContainsHuijin(List<StockDaily> initStocks) {
		Map<String, List<JSONObject>> allTop10Holders = findAllTop10Holders(0, 0);
		List<StockDaily> filteredStocks = new ArrayList<StockDaily>();
		for (StockDaily stock : initStocks) {
			List<JSONObject> holders = allTop10Holders.get(stock.getTsCode());
			if (holders == null) {
				logger.warn("Top10Holders missing for " + stock.getTsCode());
			}

			for (JSONObject holder : holders) {
				String holderName = (String) holder.get("holder_name");
				List<String> huijin = ImmutableList.of("中央汇金资产管理有限责任公司", "中国证券金融股份有限公司");

				if (holderName.startsWith("全国社会保障基金") || holderName.startsWith("全国社保基金")) {
					stock.setHolders(holderName);
					filteredStocks.add(stock);
					break;
				}
			}
		}

		logger.info("getAllHoldersContainsHuijin result count: " + filteredStocks.size());
		return filteredStocks;

	}
	
	public List<StockDaily> filteredByConcept(String con, List<StockDaily> initStocks) {
		if(StringUtils.isBlank(con) || "null".equalsIgnoreCase(con))
			return initStocks;
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", con);
		List<JSONObject> conceptDetails = mongoService.find(params, CONCEPT_DETAIL);
		
		List<StockDaily> filteredStocks = new ArrayList<StockDaily>();
		for (StockDaily stock : initStocks) {
			for (JSONObject conceptDetail : conceptDetails) {
				String code = (String) conceptDetail.get("ts_code");
				if(stock.getTsCode().equalsIgnoreCase(code)) {
					stock.setConcepts(Constant.CONCEPT_MAP_CACHE.get(con));
					filteredStocks.add(stock);
					break;
				}
				}
			}

		logger.info("filteredByConcept result count: " + filteredStocks.size());
		return filteredStocks;

	}
	
	public List<StockDaily> filteredByProfit(Float profit, List<StockDaily> initStocks) {
		Map<String, JSONObject>  stocksBasicProfits= findAllFinaIndicator(0, 0);
		
		List<StockDaily> filteredStocks = new ArrayList<StockDaily>();
		for (StockDaily stock : initStocks) {
			JSONObject stocksBasicProfit = stocksBasicProfits.get(stock.getTsCode());
			if(stocksBasicProfit == null)
				continue;
			String netprofitYoy = stocksBasicProfit.get("netprofit_yoy") == null ? "-1" : (String)stocksBasicProfit.get("netprofit_yoy");
			Float currProfit = Float.valueOf( netprofitYoy);
			if(currProfit > profit) {
				stock.setProfit(currProfit);;
				filteredStocks.add(stock);
			}
		}

		logger.info("filteredByProfit result count: " + filteredStocks.size());
		return filteredStocks;

	}

	public List<StockDaily> findStocksLower30Percent(StockDaily daily, Integer pageIndex, Integer pageSize) {
		List<Stock> allStocks = findAllStocks(0, 0);

		Float profitParam = daily.getProfit();
    	Integer totalPctChgParam = daily.getTotalPctChg();
    	String tradeDateHistParam = daily.getTradeDateHist();
    	
		List<StockDaily> stocks = new ArrayList<StockDaily>();
		Calendar calendar = Calendar.getInstance();
		Map<String, String> params = new HashMap<String, String>();
		params.put("trade_date", DateUtil.dateToStr(calendar.getTime()));
		List<JSONObject> currentDailyStocks = mongoService.find(params, STOCK_HIST_DATA);

		while (currentDailyStocks.size() == 0) {
			calendar.add(Calendar.DAY_OF_MONTH, -3);
			params.put("trade_date", DateUtil.dateToStr(calendar.getTime()));
			currentDailyStocks = mongoService.find(params, STOCK_HIST_DATA);
		}

		Date strToDate = DateUtil.strToDate(tradeDateHistParam);
		calendar.setTime(strToDate);
		params.put("trade_date", DateUtil.dateToStr(calendar.getTime()));
		List<JSONObject> histDailyStocks = mongoService.find(params, STOCK_HIST_DATA);
		while (histDailyStocks.size() == 0) {
			calendar.add(Calendar.DAY_OF_MONTH, 5);
			params.put("trade_date", DateUtil.dateToStr(calendar.getTime()));
			histDailyStocks = mongoService.find(params, STOCK_HIST_DATA);
		}

		Map<String, JSONObject> currentDailyStocksMap = new HashMap<String, JSONObject>();
		for (JSONObject dailyStock : currentDailyStocks) {
			currentDailyStocksMap.put(dailyStock.getString("ts_code"), dailyStock);
		}
		Map<String, JSONObject> histDailyStocksMap = new HashMap<String, JSONObject>();
		for (JSONObject histDailyStock : histDailyStocks) {
			histDailyStocksMap.put(histDailyStock.getString("ts_code"), histDailyStock);
		}

		for (Stock stockJson : allStocks) {
			String tsCode = stockJson.getTsCode();

			//JSONObject stockProfit = stocksBasicsJson.get(tsCode);
			JSONObject currentDailyStock = currentDailyStocksMap.get(tsCode);
			JSONObject histDailyStock = histDailyStocksMap.get(tsCode);
			if (currentDailyStock == null || histDailyStock == null)
				continue;
			Float currentPrice = currentDailyStock.getFloat("close");
			Float histPrice = histDailyStock.getFloat("close");
			//Float profit = (float) 100.0;//stockProfit.getFloat("profit");
			
			Float totalPctChg = histPrice / currentPrice - 1;
			float pctChgBenchMark = totalPctChgParam.floatValue()/100;
			if (totalPctChg < pctChgBenchMark)
				continue;

			StockDaily stock = new StockDaily();
			stock.setTsCode(stockJson.getTsCode());
			stock.setName(stockJson.getName());
			stock.setIndustry(stockJson.getIndustry());
			stock.setMarket(stockJson.getMarket());
			stock.setTradeDate(currentDailyStock.getString("trade_date"));
			stock.setTradeDateHist(histDailyStock.getString("trade_date"));
			stock.setClosePrice(currentPrice);
			stock.setClosePriceHist(histPrice);
			stock.setPctChg(currentDailyStock.getFloat("pct_chg"));
			stock.setPctChgHist(histDailyStock.getFloat("pct_chg"));
			stock.setTotalPctChg(new Float(totalPctChg * 100).intValue());
			stocks.add(stock);
		}
		
		stocks = filteredByProfit(profitParam, stocks);
		stocks = filteredByConcept(daily.getSelectedConcept(), stocks);
		
		if(daily.getHasSheBaoFunder())
			stocks = getAllHoldersContainsHuijin(stocks);
		
		
		stocks
				.sort((StockDaily h1, StockDaily h2) -> h1.getTotalPctChg().compareTo(h2.getTotalPctChg()));
		// Collections.sort(allHoldersContainsHuijin,
		// Comparator.comparing(StockDaily::getTotalPctChg));
		// Comparator<StockDaily> comparator = (h1, h2) ->
		// h1.getTotalPctChg().compareTo(h2.getTotalPctChg());
		// allHoldersContainsHuijin.sort(comparator.reversed());
		stocks.sort(Comparator.comparing(StockDaily::getTotalPctChg).reversed());
		logger.info("result count: " + stocks.size());
		return stocks;
	}

	private List<JSONObject> findHistStock(JSONObject stock, Map<String, String> params, Date currentDate) {
		// params.put("ts_code", stock.getString("ts_code"));
		params.put("trade_date", DateUtil.dateToStr(currentDate));
		return mongoService.find(params, STOCK_HIST_DATA);
	}

	private String generateQueryBody(String apiName, String param) {
		String body = "{\"api_name\": \"" + apiName + "\", \"token\": \"" + HTTP_API_TOKEN + "\", \"params\": " + param
				+ "}";
		return body;
	}
}

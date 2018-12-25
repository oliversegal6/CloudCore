package com.xyz.service;

import java.util.ArrayList;
import java.util.Calendar;
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

import com.google.gson.JsonObject;
import com.mongodb.util.JSON;
import com.xyz.pojo.Stock;
import com.xyz.pojo.StockDaily;
import com.xyz.util.DateUtil;
import com.xyz.util.HttpclientUtil;

@Component
public class StockMiningService {

	private static final String STOCK_HIST_DATA = "stockHistData";

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
		 String body = generateQueryBody("daily_basic", "{\"trade_date\":\""+trade_date+"\"}");
	     mongoService.saveBatch(HttpclientUtil.post(body, HTTP_API_TUSHARE_PRO), "dailyBasic");
	 }
	 
	 public void saveTop10Floatholders(String code) {
		 String body = generateQueryBody("top10_floatholders", "{\"ts_code\":\""+code+"\", \"start_date\":\"20180101\", \"end_date\":\"20181231\"}");
	     mongoService.saveBatch(HttpclientUtil.post(body, HTTP_API_TUSHARE_PRO), "top10Floatholders");
	 }
	 
	 public void saveAllTop10Floatholders() {
		 List<JSONObject> allStocks = mongoService.findAll(STOCK_BASIC, 0, 0);
		 for(JSONObject stock: allStocks) {
			 String code = stock.get("ts_code").toString();
			logger.info("saveStockHistData for "  + code);
			saveTop10Floatholders(code);
		 }
	 }
	 
	 public void saveStockHistDataByCode(String code) {
		 String body = generateQueryBody("daily", "{\"ts_code\":\""+code+"\", \"start_date\":\"20170101\", \"end_date\":\"20181231\"}");
	     mongoService.saveBatch(HttpclientUtil.post(body, HTTP_API_TUSHARE_PRO), STOCK_HIST_DATA);
	 }
	 
	 public void saveAllStockHistDataByCode() {
		 List<JSONObject> allStocks = mongoService.findAll(STOCK_BASIC, 0, 0);
		 for(JSONObject stock: allStocks) {
			 String code = stock.get("ts_code").toString();
			logger.info("saveStockHistData for "  + code);
			saveStockHistDataByCode(code);
		 }
	 }
	 
	 public List<Stock> findAllStocks(Integer pageIndex, Integer pageSize) {
		 List<JSONObject> stocksJson = mongoService.findAll(STOCK_BASIC, pageIndex, pageSize);
		 List<Stock> stocks = new ArrayList<Stock>();
		 for(JSONObject stockJson : stocksJson) {
			 Stock stock = new Stock();
			 stock.setTsCode(stockJson.getString("ts_code"));
			 stock.setName(stockJson.getString("name"));
			 stock.setIndustry(stockJson.getString("industry"));
			 stocks.add(stock);
		 }
		 return stocks;
	 }
	 
	 public void saveStockHistDataByDate(String date) {
		 String body = generateQueryBody("daily", "{\"trade_date\":\""+date+"\"}");
	     mongoService.saveBatch(HttpclientUtil.post(body, HTTP_API_TUSHARE_PRO), STOCK_HIST_DATA);
	 }
	 
	 public void saveAllStockHistDataByDate(String startDateStr, String endDateStr) {
		 Assert.notNull(startDateStr, "start Data can't be null");
		 Date startDate = DateUtil.strToDate(startDateStr);
		 Date endDate = StringUtils.isBlank(endDateStr) ? Calendar.getInstance().getTime() : DateUtil.strToDate(endDateStr);
		 Calendar currentCalendar = Calendar.getInstance();
		 currentCalendar.setTime(startDate);
		 
		 while (currentCalendar.getTime().before(endDate)) {
			 String date = DateUtil.dateToStr(currentCalendar.getTime());
			logger.info("saveStockHistData for "  + date);
			saveStockHistDataByDate(date);
			currentCalendar.add(Calendar.DAY_OF_MONTH, 1);
		 }
	 }
	 
	 public void saveIndexDailyByDate(String date) {
		 String body = generateQueryBody("index_daily", "{\"trade_date\":\""+date+"\", \"start_date\":\"20170101\", \"end_date\":\"20181231\"}");
	     mongoService.saveBatch(HttpclientUtil.post(body, HTTP_API_TUSHARE_PRO), STOCK_HIST_DATA);
	 }
	 
	 public void saveAllIndexDailyByDate(String startDateStr, String endDateStr) {
		 Assert.notNull(startDateStr, "start Data can't be null");
		 Date startDate = DateUtil.strToDate(startDateStr);
		 Date endDate = StringUtils.isBlank(endDateStr) ? Calendar.getInstance().getTime() : DateUtil.strToDate(endDateStr);
		 Calendar currentCalendar = Calendar.getInstance();
		 currentCalendar.setTime(startDate);
		 
		 while (currentCalendar.getTime().before(endDate)) {
			 String date = DateUtil.dateToStr(currentCalendar.getTime());
			logger.info("saveAllIndexDailyByDate for "  + date);
			saveStockHistDataByDate(date);
			currentCalendar.add(Calendar.DAY_OF_MONTH, 1);
		 }
	 }
	 
	 public List<StockDaily> findStocksLower30Percent() {
		 List<JSONObject> stocksJson = mongoService.findAll(STOCK_BASIC, 0, 0);
		 
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
		 
		 calendar.add(Calendar.YEAR, -3);
		 params.put("trade_date", DateUtil.dateToStr(calendar.getTime()));
		 List<JSONObject>  histDailyStocks = mongoService.find(params, STOCK_HIST_DATA);
		 while (histDailyStocks.size() == 0) {
			 calendar.add(Calendar.DAY_OF_MONTH, 5);
			 params.put("trade_date", DateUtil.dateToStr(calendar.getTime()));
			 histDailyStocks = mongoService.find(params, STOCK_HIST_DATA);
		 }
		 
		 Map<String, JSONObject> currentDailyStocksMap = new HashMap<String, JSONObject>();
		 for(JSONObject dailyStock : currentDailyStocks) {
			 currentDailyStocksMap.put(dailyStock.getString("ts_code"), dailyStock);
		 }
		 Map<String, JSONObject> histDailyStocksMap = new HashMap<String, JSONObject>();
		 for(JSONObject histDailyStock : histDailyStocks) {
			 histDailyStocksMap.put(histDailyStock.getString("ts_code"), histDailyStock);
		 }
		 
		 
		 for(JSONObject stockJson : stocksJson) {
			 String tsCode = stockJson.getString("ts_code");
			 
			 JSONObject currentDailyStock = currentDailyStocksMap.get(tsCode);
			 JSONObject histDailyStock = histDailyStocksMap.get(tsCode);
			 if(currentDailyStock == null || histDailyStock == null) 
				 continue;
			 Float currentPrice = currentDailyStock.getFloat("close");
			 
			 
			 Float histPrice = histDailyStock.getFloat("close");
			 
			 Float totalPctChg = histPrice/currentPrice;
			if(totalPctChg < 1.3)
				 continue;
			 
			 StockDaily stock = new StockDaily();
			 stock.setTsCode(stockJson.getString("ts_code"));
			 stock.setName(stockJson.getString("name"));
			 stock.setTradeDate(currentDailyStock.getString("trade_date"));
			 stock.setTradeDateHist(histDailyStock.getString("trade_date"));
			 stock.setClosePrice(currentPrice);
			 stock.setClosePriceHist(histPrice);
			 stock.setPctChg(currentDailyStock.getFloat("pct_chg"));
			 stock.setPctChgHist(histDailyStock.getFloat("pct_chg"));
			 stock.setTotalPctChg((totalPctChg - 1) * 100);
			 stocks.add(stock);
		 }
		 return stocks;
	 }

	private List<JSONObject> findHistStock(JSONObject stock, Map<String, String> params, Date currentDate) {
		 //params.put("ts_code", stock.getString("ts_code"));
		 params.put("trade_date", DateUtil.dateToStr(currentDate));
		 return mongoService.find(params, STOCK_HIST_DATA);
	}
	 
	 private String generateQueryBody(String apiName, String param) {
			String body = "{\"api_name\": \"" + apiName + "\", \"token\": \"" + HTTP_API_TOKEN + "\", \"params\": " + param + "}";
			return body;
		}
}

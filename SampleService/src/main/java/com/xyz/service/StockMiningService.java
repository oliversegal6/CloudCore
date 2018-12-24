package com.xyz.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.google.gson.JsonObject;
import com.mongodb.util.JSON;
import com.xyz.pojo.Stock;
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
		 List<JSONObject> allStocks = mongoService.findAll(STOCK_BASIC);
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
		 List<JSONObject> allStocks = mongoService.findAll(STOCK_BASIC);
		 for(JSONObject stock: allStocks) {
			 String code = stock.get("ts_code").toString();
			logger.info("saveStockHistData for "  + code);
			saveStockHistDataByCode(code);
		 }
	 }
	 
	 public List<Stock> findAllStocks() {
		 List<JSONObject> stocksJson = mongoService.findAll(STOCK_BASIC);
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
	 
	 private String generateQueryBody(String apiName, String param) {
			String body = "{\"api_name\": \"" + apiName + "\", \"token\": \"" + HTTP_API_TOKEN + "\", \"params\": " + param + "}";
			return body;
		}
}

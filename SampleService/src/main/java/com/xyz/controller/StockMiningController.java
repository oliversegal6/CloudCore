package com.xyz.controller;

import com.xyz.service.StockMiningScheduledJob;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonObject;
import com.xyz.dao.MongoDAO;
import com.xyz.pojo.Concept;
import com.xyz.pojo.DailyBasic;
import com.xyz.pojo.Stock;
import com.xyz.pojo.StockDaily;
import com.xyz.service.MongoService;
import com.xyz.service.StockMiningService;


@RestController
public class StockMiningController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
	private StockMiningService stockMiningService;

    @Autowired
    private StockMiningScheduledJob stockMiningScheduledJob;

    @PostMapping("/saveTodayDailyStock")
    public void saveTodayDailyStock(){
        stockMiningScheduledJob.saveTodayDailyStock();
    }

    @PostMapping("/saveAllStockBasic")
    public String saveAllStockBasic(){
        logger.info("saveAllStockBasic start...");
        stockMiningService.stockBasic();
        
        return "saveAllStockBasic done";
    }

    @PostMapping("/saveDailyBasic")
    public String saveDailyBasic(String trade_date){
        logger.info("saveDailyBasic start..." + trade_date);
        stockMiningService.saveDailyBasic(trade_date);
        return "saveDailyBasic done";
    }
    
    @PostMapping("/saveConcept")
    public String saveConcept(){
        logger.info("saveConcept start...");
        stockMiningService.saveConcept();
        
        return "saveConcept done";
    }
    
    @PostMapping("/saveConceptDetail")
    public String saveConceptDetail(){
        logger.info("saveConceptDetail start...");
        stockMiningService.saveConceptDetail();
        
        return "saveConceptDetail done";
    }
    
    @PostMapping("/saveAllTop10Floatholders")
    public String saveAllTop10Floatholders(){
        logger.info("saveAllTop10Floatholders start...");
        stockMiningService.saveAllTop10Floatholders();
        
        return "saveAllTop10Floatholders done";
    }
    
    @PostMapping("/saveFinaIndicator")
    public String saveFinaIndicator(){
        logger.info("saveFinaIndicator start...");
        stockMiningService.saveFinaIndicator();
        logger.info("saveFinaIndicator end...");
        return "saveFinaIndicator done";
    }
    
    @PostMapping("/saveAllStockHistData")
    public String saveAllStockHistData(){
        stockMiningService.saveAllStockHistDataByDate("20160101", null);
        
        return "saveAllStockHistData done";
    }

    @PostMapping("/saveStockHistDataByYear")
    public String saveStockHistDataByYear(String year) {
        stockMiningService.saveHistDailyStockByPython(year + "0101", year + "1231");
        return "saveStockHistDataByYear done";
    }

    
    @PostMapping("/saveStockHistDataFromDate")
    public String saveStockHistDataFromDate(String date){
        logger.info("saveStockHistDataFromDate start...");
        stockMiningService.saveAllStockHistDataByDate(date, null);
        
        return "saveAllStockHistData done";
    }
    
    @GetMapping("/findAllStocks")
    public Stock[] findAllStocks(@RequestParam("pageIndex") Integer pageIndex, @RequestParam("pageSize") Integer pageSize) {
    	logger.info("findAllStocks start...");
    	
    	List<Stock> results = stockMiningService.findAllStocks(pageIndex, pageSize);
    	return results.toArray(new Stock[results.size()]);
    }
    
    @GetMapping("/findDailyBasic")
    public DailyBasic[] findDailyBasic(@RequestParam("pageIndex") Integer pageIndex, @RequestParam("pageSize") Integer pageSize) {
    	logger.info("findDailyBasic start...");
    	List<DailyBasic> results = stockMiningService.findDailyBasic(pageIndex, pageSize);
    	return results.toArray(new DailyBasic[results.size()]);
    }
    
    @GetMapping("/findConcept")
    public Concept[] findConcept() {
    	logger.info("findDailyBasic start...");
    	List<Concept> results = stockMiningService.findConcept(0, 0);
    	return results.toArray(new Concept[results.size()]);
    }
    
    @PostMapping("/saveStockHistDataByDate")
    public String saveStockHistDataByDate(String date){
        logger.info("saveStockHistDataByDate start...");
        stockMiningService.saveStockHistDataByDate(date);
        
        return "saveStockHistDataByDate done";
    }
    
    @PostMapping("/saveStockHistDataByCode")
    public String saveStockHistDataByCode(String code){
        logger.info("saveStockHistDataByCode start...");
        stockMiningService.saveStockHistDataByCode(code);
        
        return "saveStockHistData done";
    }
    
    @GetMapping("/findStocksLower30Percent")
    public StockDaily[] findStocksLower30Percent(@RequestParam Map param){
    	logger.info("findAllStocks start..." + param.toString());
    	Integer pageIndex = Integer.valueOf((String)param.get("pageIndex"));
    	Integer pageSize = Integer.valueOf((String)param.get("pageSize"));
    	StockDaily daily = new StockDaily();
    	daily.setProfit(Float.valueOf( (String) param.get("profit")));
    	daily.setTotalPctChg(Integer.valueOf((String) param.get("totalPctChg")));
    	daily.setTradeDateHist((String) param.get("tradeDateHist"));
    	daily.setSelectedConcept((String) param.get("selectedConcept"));
    	
    	daily.setHasSheBaoFunder(Boolean.valueOf((String)param.get("hasSheBaoFunder")));;
        logger.info("findStocksLower30Percent start...");
        List<StockDaily> stocks = stockMiningService.findStocksLower30Percent(daily, pageIndex, pageSize);
        return stocks.toArray(new StockDaily[stocks.size()]);
    }

    @GetMapping("/sendSimpleMail")
    public void sendSimpleMail() throws Exception {
        stockMiningService.sendSimpleMail();
    }

}

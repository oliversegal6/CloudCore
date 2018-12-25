package com.xyz.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonObject;
import com.xyz.dao.MongoDAO;
import com.xyz.pojo.Stock;
import com.xyz.pojo.StockDaily;
import com.xyz.service.MongoService;
import com.xyz.service.StockMiningService;


@RestController
public class StockMiningController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
	private StockMiningService stockMiningService;
    
    @PostMapping("/stockBasic")
    public String stockBasic(){
        logger.info("stockBasic start...");
        stockMiningService.stockBasic();
        
        return "stockBasic done";
    }
    
    @PostMapping("/saveDailyBasic")
    public String saveDailyBasic(String trade_date){
        logger.info("saveDailyBasic start..." + trade_date);
        stockMiningService.saveDailyBasic(trade_date);
        
        return "saveDailyBasic done";
    }
    
    @PostMapping("/saveAllTop10Floatholders")
    public String saveAllTop10Floatholders(){
        logger.info("saveAllTop10Floatholders start...");
        stockMiningService.saveAllTop10Floatholders();
        
        return "saveAllTop10Floatholders done";
    }
    
    @PostMapping("/saveAllStockHistData")
    public String saveAllStockHistData(){
        logger.info("saveAllStockHistData start...");
        stockMiningService.saveAllStockHistDataByDate("20160101", null);
        
        return "saveAllStockHistData done";
    }
    
    @GetMapping("/findAllStocks")
    public Stock[] findAllStocks(@RequestParam("pageIndex") Integer pageIndex, @RequestParam("pageSize") Integer pageSize) {
    	logger.info("findAllStocks start...");
    	List<Stock> results = stockMiningService.findAllStocks(pageIndex, pageSize);
    	return results.toArray(new Stock[results.size()]);
    }
    
    @PostMapping("/saveStockHistData")
    public String stockHistData(String code){
        logger.info("saveStockHistData start...");
        stockMiningService.saveStockHistDataByCode(code);
        
        return "saveStockHistData done";
    }
    
    @PostMapping("/findStocksLower30Percent")
    public StockDaily[] findStocksLower30Percent(){
        logger.info("findStocksLower30Percent start...");
        List<StockDaily> stocks = stockMiningService.findStocksLower30Percent();
        return stocks.toArray(new StockDaily[stocks.size()]);
    }
}

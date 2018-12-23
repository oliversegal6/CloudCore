package com.xyz.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonObject;
import com.xyz.dao.MongoDAO;
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
    
    @PostMapping("/saveStockHistData")
    public String stockHistData(String code){
        logger.info("saveStockHistData start...");
        stockMiningService.saveStockHistDataByCode(code);
        
        return "saveStockHistData done";
    }
}

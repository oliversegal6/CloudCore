package com.xyz;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.xyz.pojo.Concept;
import com.xyz.service.StockMiningService;
import com.xyz.util.Constant;

@Component
public class InitCacheData implements ApplicationListener<ContextRefreshedEvent> {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
    private StockMiningService stockMiningService;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		logger.info("Start to initail Local Cache...");
		
		Constant.CONCEPT_CACHE = stockMiningService.findConcept(0, 0);
		logger.info("Concept Cache was Initialized!");
		for(Concept concept : Constant.CONCEPT_CACHE)
			Constant.CONCEPT_MAP_CACHE.put(concept.getCode(), concept.getName());
		
		//Constant.FINA_INDICATOR_CACHE = stockMiningService.findAllFinaIndicator(0, 0);
		logger.info("FinaIndicator Cache was Initialized!");
		//Constant.TOP10_HOLDERS_CACHE = stockMiningService.findAllTop10Holders(0, 0);
		logger.info("Top10Holders Cache was Initialized!");
		//Constant.STOCK_CACHE = stockMiningService.findAllStocks(0, 0);
		logger.info("Stock Cache was Initialized!");
		
		
		logger.info("Local Cache was Initialized!");
	}


}

package com.xyz;

import java.util.*;

import com.xyz.util.DateUtil;
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
        logger.info("Concept Cache was Initialized! Size: " + Constant.CONCEPT_CACHE.size());
        for (Concept concept : Constant.CONCEPT_CACHE)
            Constant.CONCEPT_MAP_CACHE.put(concept.getCode(), concept.getName());

        Constant.FINA_INDICATOR_CACHE = stockMiningService.findAllFinaIndicator(0, 0);
        logger.info("FinaIndicator Cache was Initialized! Size: " + Constant.FINA_INDICATOR_CACHE.size());
        Constant.TOP10_HOLDERS_CACHE = stockMiningService.findAllTop10Holders(0, 0);
        logger.info("Top10Holders Cache was Initialized! Size: " + Constant.TOP10_HOLDERS_CACHE.size());
        Constant.STOCK_CACHE = stockMiningService.findAllStocks(0, 0);
        logger.info("Stock Cache was Initialized! Size: " + Constant.STOCK_CACHE.size());
        Map<String, String> params = new HashMap<String, String>();
        params.put("trade_date", DateUtil.dateToStr(Calendar.getInstance().getTime()));
        Constant.HIST_STOCK_CACHE = stockMiningService.findHistStock(params);
        logger.info("HIST_STOCK_CACHE size: " + Constant.HIST_STOCK_CACHE.size());

        logger.info("Local Cache was Initialized!");
    }


}

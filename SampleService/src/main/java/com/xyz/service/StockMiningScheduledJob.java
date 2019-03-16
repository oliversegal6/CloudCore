package com.xyz.service;

import java.io.*;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xyz.util.Constant;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.xyz.util.DateUtil;

@Component
public class StockMiningScheduledJob {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private StockMiningService stockMiningService;


    /*
	股票列表基本信息,股票代码,行业。
	每周六刷新一次, 18点刷新
	drop -> retrieveAll -> save
	* */
    @Scheduled(cron = "0 0 18 ? * SAT")
    public void stockBasic() {
        stockMiningService.stockBasic();
    }


    /*
	股票每天最新的价格和相关信息 如PB,PE,价格等每天都要变动的价格。
	每天变动,每天18点刷新
	drop -> retrieveAll -> save
	* */
    @Scheduled(cron = "0 0 18 * * *")
    public void saveDailyBasic() {
        stockMiningService.saveDailyBasic(DateUtil.dateToStr(Calendar.getInstance().getTime()));
    }

    /*
	概念 如5G，银行。
	变动比较慢，可以每月第一天18点刷新一次
	drop -> retrieveAll -> save
	* */
    @Scheduled(cron = "0 0 18 1 * ?")
    public void saveConcept() {
        stockMiningService.saveConcept();
    }

    /*
	每只股票对应概念 如5G，银行。
	变动比较慢，可以每月第一天18点刷新一次
	drop -> retrieveAll -> save
	* */
    @Scheduled(cron = "0 0 18 1 * ?")
    public void saveConceptDetail() {
        stockMiningService.saveConceptDetail();
    }

    /*
	每只股票10大流通股东。
	变动比较慢，可以每季度刷新一次
	drop -> retrieveAll -> save
	* */
    @Scheduled(cron = "0 0 18 1 * ?")
    public void saveAllTop10Floatholders() {
        stockMiningService.saveAllTop10Floatholders();
    }

    /*
      每只股票季报。
      变动比较慢，可以每季度刷新一次
      retrieveLastQuarter -> save
    * */
 void saveFinaIndicator() {
        stockMiningService.saveFinaIndicator();
    }

    /*
	每只股票历史价格涨跌幅等均线数据。
	每天变动,每天18点刷新
	retrieveToday's data -> save
	* */
    @Scheduled(cron = "0 0 21 * * *")
    public void saveTodayDailyStock() {
        Calendar calendar = Calendar.getInstance();

        String endDate = DateUtil.dateToStr(calendar.getTime());
        calendar.add(Calendar.YEAR, -1);
        calendar.add(Calendar.MONTH, -1);
        String startDate = DateUtil.dateToStr(calendar.getTime());

        stockMiningService.saveHistDailyStockByPython(startDate, endDate, "True");

        Map<String, String> params = new HashMap<String, String>();
        params.put("trade_date", startDate);
        Constant.HIST_STOCK_CACHE = stockMiningService.findHistStock(params);
        logger.info("HIST_STOCK_CACHE size: " + Constant.HIST_STOCK_CACHE.size());

    }

    public void saveIndexDailyByDate() {
        stockMiningService.saveIndexDailyByDate(DateUtil.dateToStr(Calendar.getInstance().getTime()));
    }

    @Scheduled(cron = "0 0 23 ? * Fri")
    public void generateReport() {
        stockMiningService.generatePdfReport();
        stockMiningService.sendMimeMessageMail("oliversegal@163.com");
    }


}

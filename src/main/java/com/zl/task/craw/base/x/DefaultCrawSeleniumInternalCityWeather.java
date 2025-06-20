package com.zl.task.craw.base.x;

import com.zl.task.impl.ExecutorTaskService;

public class DefaultCrawSeleniumInternalCityWeather {
    private static ExecutorTaskService executor;

    static {
        try {
            executor = new CrawSeleniumInternalCityWeather();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ExecutorTaskService getInstance() throws Exception {
        return executor;
    }
}

package com.zl.task.craw.keyword;

import com.zl.task.impl.ExecutorTaskService;

public class DefaultCrawSeleniumDouHot {
    private static ExecutorTaskService executor;

    static {
        try {
            executor = new CrawSeleniumDouHotSearchKeyWords();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ExecutorTaskService getInstance() throws Exception {
        return executor;
    }
}

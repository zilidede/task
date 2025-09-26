package com.ecommerce.craw.dy.shop.compass;

import com.ll.drissonPage.page.ChromiumTab;
import com.zl.task.craw.list.CrawSeleniumDouYinList;
import com.zl.task.impl.ExecutorTaskService;

public class DefaultCrawSeleniumDouYinShopCompassHourLive {
    private static CrawDouYinShopCompassHourLive executor;

    static {
        try {
            executor = new CrawDouYinShopCompassHourLive();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ExecutorTaskService getInstance() throws Exception {
        return executor;
    }

    public static void setTab(ChromiumTab tab) {
        executor.setTab(tab);
    }


    public static void setHour(Integer hour) {
        executor.setHour(hour);
    }
}

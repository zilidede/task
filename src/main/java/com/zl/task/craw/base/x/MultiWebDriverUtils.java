package com.zl.task.craw.base.x;

import com.ll.drissonPage.page.ChromiumPage;
import com.ll.drissonPage.page.ChromiumTab;
import com.zl.config.ConfigIni;
import com.zl.task.craw.keyword.CrawSeleniumDouHotSearchKeyWords;
import com.zl.task.vo.task.taskResource.TaskVO;
import com.zl.utils.webdriver.WebDriverUtils;

//多线程标签页-热点宝
public class MultiWebDriverUtils {

    public static void multipleThreads() throws InterruptedException {
        //爬取热点宝搜索词 -

        Integer page = 0;
        WebDriverUtils webDriverUtils = new WebDriverUtils(ConfigIni.DRIVE_PATH, 9223);
        ChromiumPage chromiumPage = webDriverUtils.getDriver();
        chromiumPage.newTab("https://douhot.douyin.com/square/hotspot?active_tab=hotspot_search&date_window=1&sub_type=3001");
        Thread.sleep(1000 * 12);
        chromiumPage.newTab("https://douhot.douyin.com/square/hotspot?active_tab=hotspot_search&date_window=1&sub_type=3001");
        Thread.sleep(1000 * 12);
        chromiumPage.newTab("https://douhot.douyin.com/square/hotspot?active_tab=hotspot_search&date_window=1&sub_type=3001");
        Thread.sleep(1000 * 12);
        System.out.println(chromiumPage.tabsCount());
        ChromiumTab tabl = webDriverUtils.getDriver().getTab(1);
        ChromiumTab tab2 = webDriverUtils.getDriver().getTab(2);
        ChromiumTab tab3 = webDriverUtils.getDriver().getTab(3);

        //  webDriverUtils.getDriver().newTab("https://douhot.douyin.com/square/hotspot?active_tab=hotspot_search&date_window=1&sub_type=3001");
        // Thread.sleep(1000*12);
        // ChromiumTab tab4=webDriverUtils.getDriver().getTab(3);

        ParameterizedThread1 thread1 = new ParameterizedThread1(tabl, page++);
        ParameterizedThread1 thread2 = new ParameterizedThread1(tab2, page++);
        ParameterizedThread1 thread3 = new ParameterizedThread1(tab3, page++);
        //  ParameterizedThread1 thread4 = new ParameterizedThread1(tab4);
        thread1.start();
        Thread.sleep(1000 * 10);
        thread2.start();
        Thread.sleep(1000 * 10);
        // thread3.start();
        //  thread4.start();
    }

    static class ParameterizedThread extends Thread {
        private final ChromiumTab tab;

        public ParameterizedThread(ChromiumTab tab) {
            this.tab = tab;
        }

        @Override
        public void run() {
            //天气
            try {
                CrawSeleniumInternalCityWeather crawSeleniumInternalCityWeather = new CrawSeleniumInternalCityWeather();
                crawSeleniumInternalCityWeather.craw();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    static class ParameterizedThread1 extends Thread {
        private final ChromiumTab tab;
        private final int page;

        public ParameterizedThread1(ChromiumTab tab, int page) {
            this.tab = tab;
            this.page = page;
        }

        @Override
        public void run() {
            //热点宝
            try {
                CrawSeleniumDouHotSearchKeyWords crawSeleniumDouHot = new CrawSeleniumDouHotSearchKeyWords();
                crawSeleniumDouHot.setTab(tab);
                crawSeleniumDouHot.setPage(400 * page);
                crawSeleniumDouHot.run(new TaskVO(1, "test"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

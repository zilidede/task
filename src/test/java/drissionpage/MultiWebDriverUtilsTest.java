package drissionpage;

import com.ll.drissonPage.page.ChromiumTab;
import com.zl.task.craw.base.x.CrawSeleniumInternalCityWeather;
import com.zl.task.craw.keyword.CrawSeleniumDouHotSearchKeyWords;
import com.zl.task.vo.task.TaskVO;
import com.zl.utils.webdriver.WebDriverUtils;

//多线程标签页
public class MultiWebDriverUtilsTest {
    public static Integer page = 0;

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

        public ParameterizedThread1(ChromiumTab tab) {
            this.tab = tab;
        }

        @Override
        public void run() {
            //热点宝
            try {
                CrawSeleniumDouHotSearchKeyWords crawSeleniumDouHot = new CrawSeleniumDouHotSearchKeyWords();
                crawSeleniumDouHot.setTab(tab);
                crawSeleniumDouHot.setPage(400 * page++);
                crawSeleniumDouHot.run(new TaskVO(1, "test"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        //爬取热点宝搜索词 -
        WebDriverUtils webDriverUtils = new WebDriverUtils("", 9222);
        webDriverUtils.getDriver().get("https://douhot.douyin.com/square/hotspot?active_tab=hotspot_search&date_window=1&sub_type=3001");
        ChromiumTab tabl = webDriverUtils.getDriver().getTab();
        webDriverUtils.getDriver().newTab("https://douhot.douyin.com/square/hotspot?active_tab=hotspot_search&date_window=1&sub_type=3001");
        Thread.sleep(1000 * 12);
        ChromiumTab tab2 = webDriverUtils.getDriver().getTab(1);
        //   webDriverUtils.getDriver().newTab("https://douhot.douyin.com/square/hotspot?active_tab=hotspot_search&date_window=1&sub_type=3001");
        //    Thread.sleep(1000*12);
        //    ChromiumTab tab3=webDriverUtils.getDriver().getTab(2);
        //  webDriverUtils.getDriver().newTab("https://douhot.douyin.com/square/hotspot?active_tab=hotspot_search&date_window=1&sub_type=3001");
        // Thread.sleep(1000*12);
        // ChromiumTab tab4=webDriverUtils.getDriver().getTab(3);

        ParameterizedThread1 thread1 = new ParameterizedThread1(tabl);
        ParameterizedThread1 thread2 = new ParameterizedThread1(tab2);
        // ParameterizedThread1 thread3 = new ParameterizedThread1(tab3);
        //  ParameterizedThread1 thread4 = new ParameterizedThread1(tab4);
        thread1.start();
        thread2.start();
        // thread3.start();
        //  thread4.start();
    }
}

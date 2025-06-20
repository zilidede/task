package zl.task.craw.weather;

import com.ll.drissonPage.page.ChromiumTab;
import com.zl.task.craw.base.x.CrawSeleniumInternalCityWeather;
import com.zl.utils.webdriver.DefaultWebDriverUtils;
import org.junit.Test;

public class CrawSeleniumInternalCityWeatherTest {

    @Test
    public void run() {
        String url = "http://www.weather.com.cn/textFC/hb.shtml";
        DefaultWebDriverUtils.getInstance().getDriver().get(url);
        ChromiumTab tabl = DefaultWebDriverUtils.getInstance().getDriver().getTab();
        try {
            CrawSeleniumInternalCityWeather crawSeleniumInternalCityWeather = new CrawSeleniumInternalCityWeather();
            crawSeleniumInternalCityWeather.craw();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
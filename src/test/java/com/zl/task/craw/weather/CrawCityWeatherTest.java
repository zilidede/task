package com.zl.task.craw.weather;

import com.zl.task.vo.task.taskResource.DefaultTaskResourceCrawTabList;
import com.zl.task.vo.task.taskResource.TaskVO;
import org.junit.Before;
import org.junit.Test;

public class CrawCityWeatherTest {
    CrawCityWeather crawler;

    @Before
    public void set() throws Exception {
        crawler = new CrawCityWeather();
        crawler.setTab(DefaultTaskResourceCrawTabList.getTabList().get(0));
    }

    @Test
    public void test() throws Exception {
        crawler.run(new TaskVO(1, "爬取天气小时榜"));
    }

    @Test
    public void getCitys() {
        try {
            crawler.getCitys();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void crawNowCityWeather() {
    }
}
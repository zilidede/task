package com.zl.task.main;

import com.zl.task.craw.weather.CrawCityWeather;
import com.zl.task.vo.task.taskResource.DefaultTaskResourceCrawTabList;
import com.zl.task.vo.task.taskResource.TaskVO;

//爬取天气大盘
public class CrawWeatherTestCase {
    public static void main(String[] args) throws Exception {
        crawHourCityWeather();
    }

    public static void crawHourCityWeather() throws Exception {
        CrawCityWeather crawler = new CrawCityWeather();
        crawler.setTab(DefaultTaskResourceCrawTabList.getTabList().get(0));
        crawler.run(new TaskVO(1, "爬取天气小时榜"));
    }
}

package com.zl.task.main;

import com.zl.task.craw.weather.CrawCityWeather;
import com.zl.task.vo.task.taskResource.DefaultTaskResourceCrawTabList;
import com.zl.task.vo.task.taskResource.TaskVO;
import com.zl.utils.log.LoggerUtils;

//爬取天气大盘
public class CrawWeatherTestCase {
    public static void main(String[] args) throws Exception {
        crawHourCityWeather();
    }

    public static void crawHourCityWeather() throws Exception {
        LoggerUtils.logger.debug("初始化CrawCityWeather对象");
        CrawCityWeather crawler;
        try {
            crawler = new CrawCityWeather();
            crawler.setTab(DefaultTaskResourceCrawTabList.getTabList().get(0));
        }
        catch (Exception e){
            LoggerUtils.logger.error("初始化CrawCityWeather对象失败");
            throw new RuntimeException(e);
        }
        LoggerUtils.logger.debug("开始爬取天气小时榜");
        crawler.run(new TaskVO(1, "爬取天气小时榜"));
        LoggerUtils.logger.debug("爬取天气小时榜结束");
    }
}

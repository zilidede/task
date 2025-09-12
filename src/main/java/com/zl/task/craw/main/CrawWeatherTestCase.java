package com.zl.task.craw.main;

import com.zl.task.craw.SaveXHR;
import com.zl.task.craw.weather.CrawCityWeather;
import com.zl.task.craw.weather.CrawDatashareclubWeather;
import com.zl.task.vo.task.taskResource.DefaultTaskResourceCrawTabList;
import com.zl.task.vo.task.taskResource.TaskVO;
import com.zl.utils.log.LoggerUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//爬取天气大盘
public class CrawWeatherTestCase {
    public static void main(String[] args) throws Exception {
        crawHourCityWeather();
    }

    public static void crawHourCityWeather() throws Exception {
        LoggerUtils.logger.debug("初始化CrawCityWeather对象");
        crawAllWeather();
        LoggerUtils.logger.debug("爬取天气小时榜结束");
    }
    public static void crawAllWeather() throws Exception {
        CrawCityWeather crawler;
        CrawDatashareclubWeather crawDatashareclubWeather;
        try {
            crawler = new CrawCityWeather();
            crawDatashareclubWeather  = new CrawDatashareclubWeather(DefaultTaskResourceCrawTabList.getTabList().get(1));
            crawler.setTab(DefaultTaskResourceCrawTabList.getTabList().get(0));
        }
        catch (Exception e){
            LoggerUtils.logger.error("初始化CrawCityWeather对象失败");
            throw new RuntimeException(e);
        }
        LoggerUtils.logger.debug("开始爬取天气小时榜");
        LoggerUtils.logger.debug("开始爬取城市天气数据");
        List<String> strings = crawler.getCitys();
        List<String> cityUrls = new ArrayList<>();
        for (String city : strings) {
            cityUrls.add("https://weather.cma.cn/web/weather/" + city + ".html");
        }
        LoggerUtils.logger.info("开始爬取的城市天气数据总数：" + cityUrls.size());
        int countTag = 6;  //爬取任务标志
        int max = 100;
        int i=0;
        for (String cityUrl : cityUrls) {

            //爬取中国气象局城市天气
            try {
                crawler.getTab().get(cityUrl);
                Thread.sleep(1000 * 2);

            } catch (Exception e) {
                e.printStackTrace();
                LoggerUtils.logger.warn("爬取城市天气失败：" + cityUrl);
            }
            if(i++>max){
                // 爬取历史天气
                crawHistoryWeather( crawDatashareclubWeather, countTag++);
            //dowork
                Thread.sleep(1000*60*40);
            }
            SaveXHR.saveXhr( crawler.getTab(),  crawler.getXhrSaveDir(),  crawler.getXhrList());
        }
        LoggerUtils.logger.info("爬取的城市天气小时级数据已完成");
    }
    public static void crawHistoryWeather( CrawDatashareclubWeather crawDatashareclubWeather,int count) throws Exception {
        LoggerUtils.logger.debug("开始爬取城市历史天气数据");
        Map<String,String> map = crawDatashareclubWeather.crawCityWeathers(count);
        LoggerUtils.logger.debug("开始爬取城市历史天气数据已完成:"+count);
    }
}

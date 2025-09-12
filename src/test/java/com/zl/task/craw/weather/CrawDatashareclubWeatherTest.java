package com.zl.task.craw.weather;

import com.zl.task.vo.task.taskResource.DefaultTaskResourceCrawTabList;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CrawDatashareclubWeatherTest {

    @Test
    void craw() throws Exception {
        CrawDatashareclubWeather crawDatashareclubWeather  = new CrawDatashareclubWeather(DefaultTaskResourceCrawTabList.getTabList().get(0));
        crawDatashareclubWeather.craw();

    }

    @Test
    void getCityMap() throws SQLException {
        CrawDatashareclubWeather crawDatashareclubWeather  = new CrawDatashareclubWeather(DefaultTaskResourceCrawTabList.getTabList().get(0));
        crawDatashareclubWeather.getCityMap();

    }

    @Test
    void crawCityWeathers() throws Exception {
        CrawDatashareclubWeather crawDatashareclubWeather  = new CrawDatashareclubWeather(DefaultTaskResourceCrawTabList.getTabList().get(0));
        Map<String,String> map = crawDatashareclubWeather.crawCityWeathers(1);
        // 在方法结束前添加如下代码
        try (PrintWriter out = new PrintWriter(new FileWriter("./data/task/cityUrlsMap.txt"))) {
            for (Map.Entry<String,String> entry : map.entrySet()) {
                out.println(entry.getKey() + "=" + entry.getValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
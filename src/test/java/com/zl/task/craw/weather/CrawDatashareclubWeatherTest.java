package com.zl.task.craw.weather;

import com.zl.task.vo.task.taskResource.DefaultTaskResourceCrawTabList;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CrawDatashareclubWeatherTest {

    @Test
    void craw() throws Exception {
        CrawDatashareclubWeather crawDatashareclubWeather  = new CrawDatashareclubWeather(DefaultTaskResourceCrawTabList.getTabList().get(0));
        crawDatashareclubWeather.craw();

    }
}
package com.zl.task.save.weather;

import com.zl.task.save.parser.weather.SaverCityWeather;
import org.junit.Test;

public class SaverCityWeatherTest {

    @Test
    public void parserHeader() {
        try {
            SaverCityWeather.parser("S:\\data\\task\\爬虫\\weather\\");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
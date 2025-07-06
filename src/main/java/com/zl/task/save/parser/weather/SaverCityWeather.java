package com.zl.task.save.parser.weather;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zl.dao.generate.CityWeatherDO;
import com.zl.task.save.parser.ParserJsonToHttpVO;
import com.zl.task.vo.http.HttpVO;
import com.zl.utils.io.DiskIoUtils;
import com.zl.utils.log.LoggerUtils;
import com.zl.utils.time.SimpleDateFormatUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SaverCityWeather {
    private List<Map<Integer, String>> cityMaps; //城市id和名称;

    public static List<Map<String, CityWeatherDO>> parser(String srcDir) throws Exception {
        String dir = srcDir + "weather-DESKTOP-RBM0GP7\\";
        if (!DiskIoUtils.isExist(dir)) {
            LoggerUtils.logger.error("文件不存在:" + dir);
        }
        List<Map<String, CityWeatherDO>> cityMaps = new ArrayList<>();
        SimpleDateFormatUtils.setDateFormat("yyyy/MM/dd HH");
        List<String> files = DiskIoUtils.getFileListFromDir(dir + "weather.cma.cnApiNow\\");
        for (String file : files) {
            HttpVO httpVO = ParserJsonToHttpVO.parserXHRJson(file);
            String url = httpVO.getUrl();
            String json = httpVO.getResponse().getBody();
            String s = url.substring(url.indexOf("now/") + 4, url.indexOf("now/") + 9);
            CityWeatherDO cWDO = parserJson(json, s);
            if(cWDO == null)
                continue;
            Map<String, CityWeatherDO> map = new HashMap<>();
            map.put(s + "-" + cWDO.getLastUpdate(), cWDO);
            cityMaps.add(map);
        }
        return cityMaps;
    }

    private static CityWeatherDO parserJson(String json, String cityId) {
        JsonParser parser = new JsonParser();
        JsonObject object = null;
        CityWeatherDO cWDO = new CityWeatherDO();
        try {
            object = parser.parse(json).getAsJsonObject();
        } catch (Exception e) {
            e.printStackTrace();
            LoggerUtils.logger.error("解析城市天气json失败:" + json);
            return null;
        }
        cWDO.setCityId(cityId);
        cWDO.setCityName(object.get("data").getAsJsonObject().get("location").getAsJsonObject().get("path").getAsString());
        cWDO.setPrecipitation(object.get("data").getAsJsonObject().get("now").getAsJsonObject().get("precipitation").getAsDouble());
        cWDO.setTemperature(object.get("data").getAsJsonObject().get("now").getAsJsonObject().get("temperature").getAsDouble());
        cWDO.setPressure(object.get("data").getAsJsonObject().get("now").getAsJsonObject().get("pressure").getAsDouble());
        cWDO.setHumidity(object.get("data").getAsJsonObject().get("now").getAsJsonObject().get("humidity").getAsDouble());
        cWDO.setWindDirectionDegree(object.get("data").getAsJsonObject().get("now").getAsJsonObject().get("windDirectionDegree").getAsDouble());
        cWDO.setWindSpeed(object.get("data").getAsJsonObject().get("now").getAsJsonObject().get("windSpeed").getAsDouble());
        cWDO.setFeelst(object.get("data").getAsJsonObject().get("now").getAsJsonObject().get("feelst").getAsDouble());
        cWDO.setWindScale(object.get("data").getAsJsonObject().get("now").getAsJsonObject().get("windScale").getAsString());
        cWDO.setWindDirection(object.get("data").getAsJsonObject().get("now").getAsJsonObject().get("windDirection").getAsString());
        try {
            String s = object.get("data").getAsJsonObject().get("lastUpdate").getAsString();
            cWDO.setLastUpdate(SimpleDateFormatUtils.parserDate(s));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cWDO;
    }
}

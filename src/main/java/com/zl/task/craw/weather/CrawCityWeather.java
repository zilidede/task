package com.zl.task.craw.weather;

import com.ll.drissonPage.base.By;
import com.ll.drissonPage.element.ChromiumElement;
import com.zl.dao.generate.CityWeatherDO;
import com.zl.dao.generate.CityWeatherDao;
import com.zl.task.craw.SaveXHR;
import com.zl.task.craw.list.CrawBaseDouYinList;
import com.zl.task.save.parser.weather.SaverCityWeather;
import com.zl.task.vo.task.taskResource.TaskVO;
import com.zl.utils.io.FileIoUtils;
import com.zl.utils.jdbc.generator.jdbc.DefaultDatabaseConnect;
import com.zl.utils.log.LoggerUtils;
import com.zl.utils.other.Ini4jUtils;

import java.sql.SQLException;
import java.util.*;

//爬取国内城市天气数据
//https://weather.cma.cn/web/weather/
public class CrawCityWeather extends CrawBaseDouYinList {
    private final CityWeatherDao daoService;

    public CrawCityWeather() throws Exception {
        Ini4jUtils.loadIni("./data/config/config.ini");
        Ini4jUtils.setSectionValue("weather");
        setXhrSaveDir(Ini4jUtils.readIni("xhrDir"));
        Ini4jUtils.loadIni("./data/task/xhr.ini");
        setXhrList(Ini4jUtils.traSpecificSection("weather"));
        daoService = new CityWeatherDao();

    }

    public void run(TaskVO task) throws Exception {
        LoggerUtils.logger.debug("开始爬取城市天气数据");
        List<String> strings = getCitys();
        List<String> cityUrls = new ArrayList<>();
        for (String city : strings) {
            cityUrls.add("https://weather.cma.cn/web/weather/" + city + ".html");
        }
        LoggerUtils.logger.info("开始爬取的城市天气数据总数：" + cityUrls.size());
        for (String cityUrl : cityUrls) {
            try {
                getTab().get(cityUrl);
                Thread.sleep(1000 * 2);
                SaveXHR.saveXhr(getTab(), getXhrSaveDir(), getXhrList());
            } catch (Exception e) {
                e.printStackTrace();
                LoggerUtils.logger.warn("爬取城市天气失败：" + cityUrl);
            }
        }
        LoggerUtils.logger.info("爬取的城市天气小时级数据已完成");

    }

    //从数据库获取城市ids
    public List<String> getCityIdsFromPgsql() throws SQLException {
        return daoService.findCityIds();
    }

    public List<String> getCitys() throws Exception {
        setListonXhr(); //监听xhr
        String s = FileIoUtils.readFile("./data/task/weather");
        String[] citys = s.split("-");
        if (citys.length > 1) {
            //从文字提前
            List<String> list = new ArrayList<>();
            Collections.addAll(list, citys);
            return list;
        } else {
            List<String> list = getCityIdsFromPgsql();
            if (list.size() > 0) {
                return list;
            }
            //爬取网站获取citys 列表
            openEnterUrl("https://weather.cma.cn/web/weather/54511.html");
            String xpath = "//*[@class=\"dropdown pull-left\"]"; //  获取城市列表三选择框
            List<ChromiumElement> elements = getTab().eles(By.xpath(xpath));
            //国内/国外
            /*

             */
            elements.get(1).click().click(); //打开省市选择框
            Thread.sleep(1000);
            // 获取省市选择框
            xpath = "//*[@class=\"dropdown-menu province-select\"]/li";
            List<ChromiumElement> elements2 = getTab().eles(By.xpath(xpath)); //城市下拉选择框
            for (int i = 0; i < elements2.size(); i++) {
                String text = elements2.get(i).text();
                elements2.get(i).click().click();
                Thread.sleep(2000);
                // 获取城市区县选择框
                xpath = "//*[@class=\"dropdown-menu station-select\"]/li";
                List<ChromiumElement> elements3 = getTab().eles(By.xpath(xpath));
                for (int j = 0; j < elements3.size(); j++) {
                    String text2 = elements3.get(j).text();
                    elements3.get(j).click().click();
                    Thread.sleep(2000);
                    //重新打开选择框
                    xpath = "//*[@class=\"dropdown pull-left\"]"; //  获取城市列表三选择框
                    elements = getTab().eles(By.xpath(xpath));
                    try {
                        elements.get(2).click().click(); //打开省市选择框
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Thread.sleep(2000);
                    xpath = "//*[@class=\"dropdown-menu station-select\"]/li";
                    elements3 = getTab().eles(By.xpath(xpath));
                    Thread.sleep(2000);
                }
                //重新打开选择框
                xpath = "//*[@class=\"dropdown pull-left\"]"; //  获取城市列表三选择框
                elements = getTab().eles(By.xpath(xpath));
                elements.get(1).click().click(); //打开省市选择框
                Thread.sleep(2000);
                xpath = "//*[@class=\"dropdown-menu province-select\"]/li";
                elements2 = getTab().eles(By.xpath(xpath)); //城市下拉选择框
                Thread.sleep(2000);
                SaveXHR.saveXhr(getTab(), getXhrSaveDir(), getXhrList());
            }
            //解析json文本并保存到weather文件中
            List<Map<String, CityWeatherDO>> maps = SaverCityWeather.parser(getXhrSaveDir());
            Map<String, String> cityMaps = new HashMap<>();
            for (Map<String, CityWeatherDO> map : maps) {
                for (String key : map.keySet()) {
                    CityWeatherDO cityWeatherDO = map.get(key);
                    cityMaps.put(cityWeatherDO.getCityId(), cityWeatherDO.getCityName());
                }
            }
            list = new ArrayList<>();
            for (String key : cityMaps.keySet()) {
                list.add(cityMaps.get(key));
            }
            String result = String.join("-", list);
            FileIoUtils.writeToFile("./data/task/weather", result);
            return loadCityWeathers();
        }

    }

    public List<String> crawNowCityWeather() throws Exception {
        //爬取网站获取citys 列表
        openEnterUrl("https://weather.cma.cn/web/weather/54511.html");
        String xpath = "//*[@class=\"dropdown pull-left\"]"; //  获取城市列表三选择框
        List<ChromiumElement> elements = getTab().eles(By.xpath(xpath));
        //国内/国外
        /*
         */
        elements.get(1).click().click(); //打开省市选择框
        Thread.sleep(1000);
        // 获取省市选择框
        xpath = "//*[@class=\"dropdown-menu province-select\"]/li";
        List<ChromiumElement> elements2 = getTab().eles(By.xpath(xpath)); //城市下拉选择框
        for (int i = 0; i < elements2.size(); i++) {
            String text = elements2.get(i).text();
            elements2.get(i).click().click();
            Thread.sleep(2000);
            // 获取城市区县选择框
            xpath = "//*[@class=\"dropdown-menu station-select\"]/li";
            List<ChromiumElement> elements3 = getTab().eles(By.xpath(xpath));
            for (int j = 0; j < elements3.size(); j++) {
                String text2 = elements3.get(j).text();
                elements3.get(j).click().click();
                Thread.sleep(2000);
                //重新打开选择框
                xpath = "//*[@class=\"dropdown pull-left\"]"; //  获取城市列表三选择框
                elements = getTab().eles(By.xpath(xpath));
                try {
                    elements.get(2).click().click(); //打开省市选择框
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Thread.sleep(2000);
                xpath = "//*[@class=\"dropdown-menu station-select\"]/li";
                elements3 = getTab().eles(By.xpath(xpath));
                Thread.sleep(2000);
            }
            //重新打开选择框
            xpath = "//*[@class=\"dropdown pull-left\"]"; //  获取城市列表三选择框
            elements = getTab().eles(By.xpath(xpath));
            elements.get(1).click().click(); //打开省市选择框
            Thread.sleep(2000);
            xpath = "//*[@class=\"dropdown-menu province-select\"]/li";
            elements2 = getTab().eles(By.xpath(xpath)); //城市下拉选择框
            Thread.sleep(2000);
            SaveXHR.saveXhr(getTab(), getXhrSaveDir(), getXhrList());
        }
        //解析json文本并保存到weather文件中
        List<Map<String, CityWeatherDO>> maps = SaverCityWeather.parser(getXhrSaveDir());
        Map<String, String> cityMaps = new HashMap<>();
        for (Map<String, CityWeatherDO> map : maps) {
            for (String key : map.keySet()) {
                CityWeatherDO cityWeatherDO = map.get(key);
                cityMaps.put(cityWeatherDO.getCityId(), cityWeatherDO.getCityName());
            }
        }
        List<String> list = new ArrayList<>();
        for (String key : cityMaps.keySet()) {
            list.add(cityMaps.get(key));
        }
        String result = String.join("-", list);
        FileIoUtils.writeToFile("./data/task/weather", result);
        return loadCityWeathers();
    }

    //解析city weatherjson文本并保存到weather文件中
    public List<String> loadCityWeathers() throws Exception {
        List<Map<String, CityWeatherDO>> maps = SaverCityWeather.parser(getXhrSaveDir());
        Map<String, String> cityMaps = new HashMap<>();
        for (Map<String, CityWeatherDO> map : maps) {
            for (String key : map.keySet()) {
                CityWeatherDO cityWeatherDO = map.get(key);
                cityMaps.put(cityWeatherDO.getCityId(), cityWeatherDO.getCityName());
            }
        }
        List<String> list = new ArrayList<>();
        for (String key : cityMaps.keySet()) {
            list.add(cityMaps.get(key));
        }
        String result = String.join("-", list);
        FileIoUtils.writeToFile("./data/task/weather", result);
        return list;
    }


}

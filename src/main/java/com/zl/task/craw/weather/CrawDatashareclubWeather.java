package com.zl.task.craw.weather;

import com.ll.drissonPage.base.By;
import com.ll.drissonPage.element.ChromiumElement;
import com.ll.drissonPage.page.ChromiumTab;
import com.zl.dao.generate.CityWeatherDao;
import com.zl.task.craw.SaveXHR;
import com.zl.task.craw.base.CrawBaseXHR;
import com.zl.utils.log.LoggerUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// https://datashareclub.com/area/%E4%B8%8A%E6%B5%B7/%E4%B8%8A%E6%B5%B7.html 爬取历史天气数据
public class CrawDatashareclubWeather  extends CrawBaseXHR {
    private CityWeatherDao cityWeatherDao;

    public CrawDatashareclubWeather(ChromiumTab tab) throws SQLException {
        super(tab);
        cityWeatherDao=new CityWeatherDao();
    }

    @Override
    public void craw() throws Exception {
        init("weather");
        getTab().listen().start(getXhrList());
        // 遍历省市级别天气
        Map<String, String> map = getCityMap();
        for(Map.Entry<String, String> entry: map.entrySet()){
            String[] citys=entry.getKey().split("&");
            String url=String.format("https://datashareclub.com/area/%s/%s.html",citys[0],entry.getValue());
            openEnterUrl(url);
            Thread.sleep(5000);
            SaveXHR.saveXhr(getTab(), getXhrSaveDir(), getXhrList());

        }
    }
    public  Map<String,String>  crawCityWeathers(int count) throws Exception {
        init("weather");
        getTab().listen().start(getXhrList());
        String url=String.format("https://datashareclub.com/area/北京/北京.html");
        openEnterUrl(url);
         // 实现遍历并抓取各城市天气的逻辑
        Map<String,String> cityUrlsMap=new HashMap<>();
        String xpath="//*[@class='nav flex-column pb-5']/li";
        List<ChromiumElement> elements = getTab().eles(By.xpath(xpath));
        Thread.sleep(2000);
        int i=count;
        ChromiumElement element=elements.get(i);
        String cityName=element.text();
        element.click().click();
        Thread.sleep(1000);
        List<ChromiumElement> elements1=element.eles(By.xpath("./ul[1]/li"));
        // 遍历二级城市市
        for(int j=0;j<elements1.size();j++){
            ChromiumElement element1=elements1.get(j);
            String cityName1="";
            try {
                cityName1=element1.text();
                element1.click().click();
                Thread.sleep(1000);
                xpath="//*[@class='d-flex scrollable']/a";
                List<ChromiumElement> elements2=getTab().eles(By.xpath(xpath));
                for(int k=1;k<elements2.size();k++){
                    ChromiumElement element2=elements2.get(k);
                    String cityName2=element2.text();
                    url=String.format("https://datashareclub.com/%s",element2.attr("href"));
                    cityUrlsMap.put(cityName+"-"+cityName1+"-"+cityName2,url);
                    element2.click().click();
                    Thread.sleep(2000);
                    xpath="//*[@class='d-flex scrollable']/a";
                    elements2=getTab().eles(By.xpath(xpath));
                }
            }
             catch (Exception e){
                //错误重新设置
                xpath="//*[@class='nav flex-column pb-5']/li";
                List<ChromiumElement> element1s = getTab().eles(By.xpath(xpath));
                element=element1s.get(i);
                element.click().click();
                Thread.sleep(1000);
                elements1=element.eles(By.xpath("./ul[1]/li"));

            }
         }
        xpath="//*[@class='nav flex-column pb-5']/li";
        elements = getTab().eles(By.xpath(xpath));
        Thread.sleep(1000);
        return cityUrlsMap;

    }
    public Map<String, String> getCityMap() throws SQLException {
        List<String> citys=cityWeatherDao.findCityNames();
        Map<String, String> map = new HashMap<>();
        for(String city:citys) {
            city=city.replace(" ","");
            String [] sCitys=city.split(",");
            if(sCitys[0].equals("中国")) {
                if (sCitys.length > 2)
                    map.put(sCitys[1] + "&" + sCitys[2], sCitys[2]);
                else {
                    LoggerUtils.logger.warn("没有找到对应的城市名称：" + city);
                    continue;
                }
            }

        }
        return map;
    }
}

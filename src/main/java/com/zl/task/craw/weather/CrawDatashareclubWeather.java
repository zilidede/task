package com.zl.task.craw.weather;

import com.ll.drissonPage.page.ChromiumTab;
import com.zl.task.craw.SaveXHR;
import com.zl.task.craw.base.CrawBaseXHR;

import java.util.HashMap;
import java.util.Map;

// https://datashareclub.com/area/%E4%B8%8A%E6%B5%B7/%E4%B8%8A%E6%B5%B7.html
public class CrawDatashareclubWeather  extends CrawBaseXHR {

    public CrawDatashareclubWeather(ChromiumTab tab) {
        super(tab);
    }

    @Override
    public void craw() throws Exception {
        init("weather");
        getTab().listen().start(getXhrList());
        // 遍历省市级别天气
        Map<String, String> map = getCityMap();
        for(Map.Entry<String, String> entry: map.entrySet()){
            String url=String.format("https://datashareclub.com/area/%s/%s.html",entry.getKey(),entry.getValue());
            openEnterUrl(url);
            Thread.sleep(5000);
            SaveXHR.saveXhr(getTab(), getXhrSaveDir(), getXhrList());

        }




    }
    public Map<String, String> getCityMap(){
        Map<String, String> map = new HashMap<>();
        map.put("上海", "上海");
        return map;
    }
}

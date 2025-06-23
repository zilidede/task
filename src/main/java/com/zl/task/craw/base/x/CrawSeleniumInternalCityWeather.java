package com.zl.task.craw.base.x;


import com.ll.drissonPage.base.By;
import com.ll.drissonPage.element.ChromiumElement;
import com.ll.drissonPage.page.ChromiumTab;
import com.zl.dao.generate.InternalCityWeatherDO;
import com.zl.task.impl.ExecutorTaskService;
import com.zl.task.vo.task.taskResource.DefaultTaskResourceCrawTabList;
import com.zl.task.vo.task.taskResource.TaskResource;
import com.zl.task.vo.task.taskResource.TaskVO;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @className: com.craw.nd.service.other.person.Impl.craw.weather-> CrawInternalCityWeather
 * @description: 爬取国内主要城市天气
 * @author: zl
 * @createDate: 2023-02-08 13:53
 * @version: 1.0
 * @todo:
 */
public class CrawSeleniumInternalCityWeather implements ExecutorTaskService {

    private final List<InternalCityWeatherDO> list;
    private SimpleDateFormat sf;
    private ChromiumTab tab;


    public CrawSeleniumInternalCityWeather() throws Exception {
        list = new ArrayList<>();

    }

    public ChromiumTab getTab() {
        return tab;
    }

    public void setTab(ChromiumTab tab) {
        this.tab = tab;
    }



    @Override
    public void ExecutorTaskService(TaskResource taskResource) {

    }

    @Override
    public void ExecutorTaskService(Object object) {

    }

    @Override
    public void run(TaskVO task) throws Exception {
        tab = DefaultTaskResourceCrawTabList.getTabList().get(0);
        //  tab.get("http://www.weather.com.cn/textFC/hb.shtml");
        //   Thread.sleep(1000*10);
        craw();
    }

    public void craw() throws InterruptedException {
        //  String url = "http://www.weather.com.cn/textFC/hb.shtml";
        //  DefaultWebDriverUtils.getInstance().getUrl(url);
        String xpath = "//*[@class=\"lq_contentboxTab2\"]/li";
        List<ChromiumElement> elements = tab.eles(By.xpath(xpath));

        // elements = DefaultWebDriverUtils.getInstance().findElements(xpath);
        int m = 1;
        for (int i = 0; i < elements.size() - 1; i++) {
            //区域

            xpath = "//*[@class=\"lq_contentboxTab2\"]/li";
            elements = tab.eles(By.xpath(xpath));
            elements.get(i).click();
            String s = "";
            try {
                s = tab.ele("//*[@class=\"contentboxTab\"]/h1/a[2]").text();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            //日期
            xpath = "//*[@class=\"day_tabs\"]/li";
            List<ChromiumElement> elements1 = tab.eles(By.xpath(xpath));
            for (int j = 0; j < elements1.size(); j++) {
                elements1.get(j).click().click();
                Calendar calendar = Calendar.getInstance();
                Date now = new Date();
                calendar.setTime(now);
                calendar.add(Calendar.DATE, j);
                getCityWeather(calendar.getTime(), j + 1, j, s);
            }

        }


    }

    public void getCityWeather(Date date, Integer n, Integer l, String local) {
        String xpath = String.format("/html/body/div[4]/div[2]/div/div/div[2]/div[%d]/div", n);
        List<ChromiumElement> elements = tab.eles(By.xpath(xpath));
        for (int i = 0; i < elements.size(); i++) {
            ChromiumElement element = elements.get(i);


            String province = elements.get(i).ele(By.xpath("./table/tbody/tr[3]/td[1]/a")).text();
            xpath = "./table/tbody/tr";
            List<ChromiumElement> elements1 = elements.get(i).eles(By.xpath(xpath));
            for (int j = 2; j < elements1.size(); j++) {
                try {
                    List<ChromiumElement> element2 = elements1.get(j).eles(By.xpath("./td"));
                    InternalCityWeatherDO vo = new InternalCityWeatherDO();
                    int m = 0;
                    if (j == 2)
                        m = 1;
                    if (element2.get(m).innerHtml().equals(""))
                        continue;
                    for (int k = 0; k < element2.size(); k++) {
                        System.out.println(element2.get(k).innerHtml());
                    }
                    vo.setRecordTime(date);
                    String temp = "";
                    try {
                        temp = element2.get(m++).innerHtml();
                        temp = temp.substring(temp.indexOf("\">") + 2, temp.indexOf("</a>"));

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    vo.setCityName(local + "-" + province + "-" + temp);
                    vo.setDayMeteorological(element2.get(m++).innerHtml());
                    temp = element2.get(m++).innerHtml();
                    String s = temp.substring(temp.indexOf("<span>") + 6, temp.indexOf("</span>"));
                    s = s + temp.substring(temp.indexOf("right\">") + 7, temp.length() - 7);
                    vo.setDayWind(s);
                    try {
                        temp = element2.get(m++).innerHtml();
                        vo.setMaxAirTemperature(Integer.parseInt(temp));
                    } catch (Exception e) {
                        continue;
                    }

                    vo.setNightMeteorological(element2.get(m++).innerHtml());
                    temp = element2.get(m++).innerHtml();
                    s = "";
                    s = temp.substring(temp.indexOf("<span>") + 6, temp.indexOf("</span>"));
                    s = s + temp.substring(temp.indexOf("right\">") + 7, temp.length() - 7);
                    vo.setNightWind(s);
                    vo.setMinAirTemperature(Integer.parseInt(element2.get(m++).innerHtml()));
                    list.add(vo);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }


            }
        }
    }


}

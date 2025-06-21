package com.zl.task.craw.keyword;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ll.drissonPage.base.By;
import com.ll.drissonPage.element.ChromiumElement;
import com.ll.drissonPage.units.listener.DataPacket;
import com.zl.task.craw.base.x.CrawServiceXImpl;
import com.zl.task.impl.taskResource.DefaultTaskResourceCrawTabList;
import com.zl.task.save.parser.ParserFiddlerJson;
import com.zl.task.vo.http.HttpVO;
import com.zl.task.vo.task.TaskVO;
import com.zl.utils.io.FileIoUtils;
import com.zl.utils.log.LoggerUtils;
import com.zl.utils.other.DateUtils;
import com.zl.utils.other.Ini4jUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @className: com.craw.nd.service.other.person.Impl.craw.selenium-> CrawOceanEngineSearch
 * @description: 爬取巨量算数搜索词
 * @author: zl
 * @createDate: 2025-01-06 12：07
 * @version: 1.0
 * @todo:
 */
public class CrawTrendinsightKeywords extends CrawServiceXImpl {
    private List<String> keywords;
    private final String srcDir;

    public CrawTrendinsightKeywords() throws Exception {
        Ini4jUtils.loadIni("./data/config/config.ini");
        Ini4jUtils.setSectionValue("trendinsight");
        srcDir = Ini4jUtils.readIni("srcDir");
    }

    public static void main(String[] args) throws Exception {
        CrawTrendinsightKeywords craw = new CrawTrendinsightKeywords();
        craw.setTab(DefaultTaskResourceCrawTabList.getTabList().get(0));

        craw.run(new TaskVO(1, "爬取巨量算数关键字"));
    }

    public void run(TaskVO task) throws Exception {
        getXHRList().add("index/get_multi_keyword_interpretation");
        getXHRList().add("index/get_multi_keyword_hot_trend");
        getXHRList().add("index/get_relation_word");
        getXHRList().add("index/get_portrait");
        getTab().listen().start(getXHRList());
        crawFromFile();

    }

    public void crawRootKeyword(String rootKeyword, int deep, String startTime, String endTime) throws InterruptedException {
        //从词根爬取关键字列表-deep=深度，startTime=开始时间，endTime=结束时间;
        keywords = getKeywords("./data/task/手工词根列表.txt");
        for (String s : keywords) {
            craw(s, "2025-01-06", "2025-01-12");
        }
    }

    public void crawTopicYesterday(String keyword) {
        //爬取关键词话题题昨天数据

    }

    public void crawFromFile() throws InterruptedException {
        //从爬取文件获取关键字列表爬取
        keywords = getKeywords("./data/task/待爬取关键字列表.txt");
        for (String s : keywords)
            craw(s, "", "");
    }

    public List<String> getKeywords(String filePath) {
        List<String> result = new ArrayList<>();
        String[] content = FileIoUtils.readTxtFile(filePath, "utf-8").split("\r\n");
        Collections.addAll(result, content);
        return result;
    }

    public List<HttpVO> craw(String keyword, String startTime, String endTime) throws InterruptedException {
        //爬取单一关键词
        String url = String.format("https://trendinsight.oceanengine.com/arithmetic-index/analysis?keyword=%s&appName=aweme", keyword);
        openUrl(url, 10.00);

        if(!startTime.equals(""))
            selectTime(startTime, endTime);//选择时间差；
        String xpath = "//*[@class=\"byted-tab-bar-item byted-tab-bar-item-type-card\"]";
        List<ChromiumElement> elements = getTab().eles(By.xpath(xpath)); //获取指数 关联 画像；
        for (int i = 0; i < 2; i++) {
            elements.get(i).click().click();
            Thread.sleep(1000 * 4);
            xpath = "//*[@class=\"byted-tab-bar-item byted-tab-bar-item-type-card\"]";
            elements = getTab().eles(By.xpath(xpath));
        }
        List<HttpVO> httpVOS = new ArrayList<>();
        List<DataPacket> res = getTab().listen().waits(100, 2.1, false, true);
        if (res.size() >= 1) {
            for (DataPacket data : res)
                if (data != null)
                    try {
                        String filePath = saveXhr(srcDir, data);
                        HttpVO httpVO = ParserFiddlerJson.parserXHRJson(filePath);
                        String json = httpVO.getResponse().getBody();
                        JsonParser parser = new JsonParser();
                        JsonObject object = parser.parse(json).getAsJsonObject();
                        String jsonData =CypTrendinsightData.decrypt(object.get("data").getAsString());
                        httpVO.getResponse().setBody(jsonData);
                        httpVOS.add(httpVO);
                    } catch (Exception e) {
                        LoggerUtils.logger.info("保存文件失败：" + data.url());
                    }

        } else {
            System.out.println("error");
        }
        return httpVOS;
    }

    public void selectTime(String startDate, String endDate) throws InterruptedException {
        String xpath="//*[@class=\"byted-input byted-input-size-md\"]";
        ChromiumElement element=getTab().eles(By.xpath(xpath)).get(0); //获取日历控件元素
        System.out.println(element.attr("value"));
        element.click().click(); // 打开日历控件选择框；
        Thread.sleep(1000 * 1);

        //start date
        // DateUtils.calculateDaysFromYearMonth()
      //  String startDate="2025-01-04";
       // String endDate="2025-06-06";
        //左侧选择
        xpath="//*[@class=\"byted-date-title-item byted-date-date\"]";
        // 获取左侧标题日期
        List<ChromiumElement> eles=getTab().eles(By.xpath(xpath)); // 获取左右侧标题日期
        Thread.sleep(1000 * 1);
        for (ChromiumElement ele : eles) {
            System.out.println(ele.text());
        }
        String leftDate=eles.get(0).text()+" "+eles.get(1).text()+"01";
        leftDate=leftDate.replace("年", "-").replace("月", "-").replace(" ","");
        Long count= DateUtils.calculateMonthsBetween(leftDate,startDate);
        xpath="//*[@viewBox=\"0 0 16 16\"]";
        List<ChromiumElement> eles1=getTab().eles(By.xpath(xpath)); // 获取左右侧月份调节器
        if(count<0){
            for (int i = 0; i < -count+1; i++) {
                eles1.get(1).click().click(); //月份减一
                Thread.sleep(1000 * 2);
            }
        }
        else{
            for (int i = 0; i < count; i++) {
                eles1.get(2).click().click(); //月份加一
                Thread.sleep(1000 * 2);
            }
        }
        String []strings=startDate.split("-");
        Integer day=Integer.parseInt(strings[2].replace("0", ""));
        //选中左侧日期
        xpath="//*[@class=\"byted-date-view byted-date-date byted-date-owner-date byted-date-position-start byted-date-view-size-md\"]";
        ChromiumElement ele3=getTab().ele(By.xpath(xpath)); //获得左侧日历表
        Thread.sleep(1000 * 1);
        selectCalendarDay(startDate, ele3);

        // 获取右侧标题日期
        xpath="//*[@class=\"byted-date-title-item byted-date-date\"]";
        eles=getTab().eles(By.xpath(xpath)); // 获取左右侧标题日期
        String rightDate=eles.get(2).text()+" "+eles.get(3).text()+"01";
        rightDate=rightDate.replace("年", "-").replace("月", "-").replace(" ","");
        count=DateUtils.calculateMonthsBetween(rightDate,endDate);
        xpath="//*[@viewBox=\"0 0 16 16\"]";
        eles1=getTab().eles(By.xpath(xpath)); // 获取左右侧月份调节器
        if(count<0){
            for (int i = 0; i < -count; i++) {
                eles1.get(1).click().click(); //月份减一
                Thread.sleep(1000 * 2);
            }
        }
        else{
            for (int i = 0; i < count; i++) {
                eles1.get(2).click().click(); //月份加一
                Thread.sleep(1000 * 2);
            }
        }
        strings=endDate.split("-");
        day=Integer.parseInt(strings[2].replace("0", ""));
        //选中右侧日期
        xpath="//*[@class=\"byted-date-view byted-date-date byted-date-owner-date byted-date-position-end byted-date-view-size-md\"]";
        ele3=getTab().ele(By.xpath(xpath)); //获得右侧日历表
        Thread.sleep(1000 * 1);
        selectCalendarDay(startDate, ele3);


    }
    public void selectCalendarDay(String date, ChromiumElement ele3) throws InterruptedException {
        //选中日历日期
        String xpath = "";
        Integer day = Integer.parseInt(date.split("-")[2].replace("0", ""));
        Thread.sleep(1000 * 1);
        if (day == 1) {
            xpath = "//*[@class=\"byted-popper-trigger byted-popper-trigger-hover byted-date-col byted-date-date byted-date-col-size-md byted-date-grid-start\"]";
            ChromiumElement ele = ele3.ele(By.xpath(xpath));
            Thread.sleep(1000 * 1);
            ele.click().click();
        } else {
            List<ChromiumElement> eles3 = ele3.eles(By.xpath("//*[@class=\"byted-popper-trigger byted-popper-trigger-hover byted-date-col byted-date-date byted-date-col-size-md byted-date-grid-in\"]"));
            Thread.sleep(1000 * 1);
            for (ChromiumElement ele : eles3) {
                System.out.println(ele.text());
                Integer i = Integer.parseInt(ele.text().replace("0", ""));
                if (i == day) {
                    ele.click().click();
                    Thread.sleep(1000 * 1);
                    break;
                }
            }
        }
    }


}

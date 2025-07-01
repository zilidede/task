package com.zl.task.craw.keyword;


import com.ll.drissonPage.base.By;
import com.ll.drissonPage.element.ChromiumElement;
import com.zl.task.vo.task.taskResource.DefaultTaskResourceCrawTabList;
import com.zl.utils.other.DateUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class CrawTrendinsightKeywordsTest {
     CrawTrendinsightKeywords crawler;
    @Before
    public void init() throws Exception {
        try {
            crawler = new CrawTrendinsightKeywords();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        DefaultTaskResourceCrawTabList.setPort(9222);
        DefaultTaskResourceCrawTabList.setTabCount(2);
        crawler.setTab(DefaultTaskResourceCrawTabList.getTabList().get(1));
    }

    @Test
    public void run() {
    }

    @Test
    public void selectTimeTest() throws InterruptedException {
        crawler.openUrl("https://trendinsight.oceanengine.com/arithmetic-index/analysis?keyword=防晒衣&appName=aweme", 10.00);
        Thread.sleep(1000 * 5);
        String xpath="//*[@class=\"byted-input byted-input-size-md\"]";
        ChromiumElement element=crawler.getTab().eles(By.xpath(xpath)).get(0); //获取日历控件元素
        System.out.println(element.attr("value"));
        element.click().click(); // 打开日历控件选择框；
        Thread.sleep(1000 * 1);

         //start date
       // DateUtils.calculateDaysFromYearMonth()
        String startDate="2025-01-04";
        String endDate="2025-06-06";
        //左侧选择
        xpath="//*[@class=\"byted-date-title-item byted-date-date\"]";
        // 获取左侧标题日期
        List<ChromiumElement> eles=crawler.getTab().eles(By.xpath(xpath)); // 获取左右侧标题日期
        Thread.sleep(1000 * 1);
        for (ChromiumElement ele : eles) {
            System.out.println(ele.text());
        }
        String leftDate=eles.get(0).text()+" "+eles.get(1).text()+"01";
        leftDate=leftDate.replace("年", "-").replace("月", "-").replace(" ","");
        Long count=DateUtils.calculateMonthsBetween(leftDate,startDate);
        xpath="//*[@viewBox=\"0 0 16 16\"]";
        List<ChromiumElement> eles1=crawler.getTab().eles(By.xpath(xpath)); // 获取左右侧月份调节器
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
        ChromiumElement ele3=crawler.getTab().ele(By.xpath(xpath)); //获得左侧日历表
        Thread.sleep(1000 * 1);
        selectCalendarDay(startDate, ele3);

        // 获取右侧标题日期
        xpath="//*[@class=\"byted-date-title-item byted-date-date\"]";
        eles=crawler.getTab().eles(By.xpath(xpath)); // 获取左右侧标题日期
        String rightDate=eles.get(2).text()+" "+eles.get(3).text()+"01";
        rightDate=rightDate.replace("年", "-").replace("月", "-").replace(" ","");
        count=DateUtils.calculateMonthsBetween(rightDate,endDate);
        xpath="//*[@viewBox=\"0 0 16 16\"]";
        eles1=crawler.getTab().eles(By.xpath(xpath)); // 获取左右侧月份调节器
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
        ele3=crawler.getTab().ele(By.xpath(xpath)); //获得右侧日历表
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
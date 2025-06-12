package com.zl.task.craw.keyword;
// Created by zl on 2025-01-03.
// 爬取小红书平台聚光户搜索关键词信息

import com.ll.drissonPage.base.By;
import com.ll.drissonPage.element.ChromiumElement;
import com.ll.drissonPage.page.ChromiumTab;
import com.zl.task.impl.ExecutorTaskService;
import com.zl.task.impl.taskResource.TaskResource;
import com.zl.task.vo.task.TaskVO;
import com.zl.utils.drissonPage.ElementUtils;

import java.util.List;

public class CrawAuroraSearchKeys implements ExecutorTaskService {
    private ChromiumTab tab;

    @Override
    public void ExecutorTaskService(TaskResource taskResource) {

    }

    CrawAuroraSearchKeys(ChromiumTab tab) {
        this.tab = tab;
    }

    @Override
    public void run(TaskVO task) throws Exception {
        String name = task.getTaskName();
        //聚光搜索词下载；
        String url = "https://ad.xiaohongshu.com/microapp/traffic-guide/keywordsInsight";
        String date = "";
        openPage(url);
        String xpath = "";
        ChromiumElement element = null;
        List<ChromiumElement> elements = null;
        int size = 0;
        //选择类目名
        xpath = "//*[@class=\"d-new-cascader__content\"]";
        element = tab.ele(By.xpath(xpath)); //打开类目选择框
        Thread.sleep(1000);
        element.click().click();
        Thread.sleep(1000);
        xpath = "//*[@class=\"d-new-cascader__column\"]";
        elements = tab.eles(By.xpath(xpath)).get(2).eles(By.xpath("./div")); //获取子类目列表
        size = elements.size();
        int minSearchCount = 100;
        for (int i = 0; i < size; i++) {
            ChromiumElement e = elements.get(i);
            e.click().click();
            Thread.sleep(1000);
            System.out.println(e.text());
            //查询-下载
            downloadKeywords(); //下载类目文件
            crawReleContent(minSearchCount);//爬取搜索关键词相关笔记内容
            //重新打开类目选择框
            xpath = "//*[@class=\"d-new-cascader__content\"]";
            tab.ele(By.xpath(xpath)).click().click();
            Thread.sleep(1000 * 1);
            xpath = "//*[@class=\"d-new-cascader__column\"]";
            elements = tab.eles(By.xpath(xpath)).get(2).eles(By.xpath("./div"));
        }
        // 爬取搜索关键词搜索次数。
        url = "https://ad.xiaohongshu.com/microapp/traffic-guide/keywordAnalysis";
        openPage(url);
        List<String> keywords = getUnRecordKeywords(); //获取未记录的关键词
        for (String keyword : keywords) {
            crawKeywordCount(keyword, date); //爬取搜索关键词搜索次数。
        }
    }

    public List<String> getUnRecordKeywords() {
        return null;
    }

    public boolean openPage(String url) {
        //打开url
        tab.get(url);
        try {
            Thread.sleep(1000 * 10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public boolean downloadKeywords() throws InterruptedException {
        //下载类目文件
        String xpath = "";
        xpath = "//*[@id=\"traffic-guide-container\"]/div/div[1]/div/button[1]/div/span";
        tab.ele(By.xpath(xpath)).click().click(); //查询更新
        Thread.sleep(1000 * 5);
        xpath = "//*[@class=\"d-button-content\"]";
        tab.eles(By.xpath(xpath)).get(12).click().click(); //下载
        Thread.sleep(1000 * 1);

        return true;
    }

    public boolean crawReleContent(int minSearchCount) throws InterruptedException {
        //爬取搜索关键词相关笔记内容
        String xpath = "";
        ChromiumTab tab = ElementUtils.init();
        ChromiumElement element = null;
        List<ChromiumElement> elements = null;
        int size = 0;
        //聚光搜索词相关内容
        xpath = "//*[@class=\"d-table__body\"]";
        elements = tab.eles(By.xpath(xpath));
        elements = elements.get(1).eles(By.xpath("./tr"));
        int j = 0;
        size = elements.size();
        for (int k = 0; k < size; k++) {
            ChromiumElement element1 = elements.get(k);
            System.out.println(element1.text());
            tab.actions().moveTo(element1);
            element1.click().click();
            Thread.sleep(1000);
            if (j++ > 20) {
                xpath = "//*[@class=\"d-table__body\"]";
                elements = tab.eles(By.xpath(xpath));
                elements = elements.get(1).eles(By.xpath("./tr"));
                j = 10;
                k = 0;
            }
        }
        return true;
    }

    public boolean crawKeywordCount(String keyword, String date) throws InterruptedException {
        //爬取搜索关键词搜索次数。url="";
        String xpath = "//*[@id=\"traffic-guide-container\"]/div/div[1]/div/div[1]/button/div/span[1]/span[2]";
        tab.ele(By.xpath(xpath)).click().click(); //打开选择框
        Thread.sleep(1000);
        ChromiumElement element = tab.ele(By.xpath("//*[@class=\"d-text d-select-placeholder d-text-ellipsis d-text-nowrap\"]"));
        Thread.sleep(1000);
        element.input(keyword); //输入搜索词
        Thread.sleep(1000);
        tab.eles(By.xpath("//*[@class=\"d-option-name\"]")).get(0).click().click(); //选择第一行
        Thread.sleep(1000);
        xpath = "//*[@id=\"traffic-guide-container\"]/div/div[1]/div/div[1]/button/div/span[1]/span[2]";
        tab.ele(By.xpath(xpath)).click().click(); //关闭选项框
        Thread.sleep(1000);
        tab.ele(By.xpath("//*[@id=\"traffic-guide-container\"]/div/div[1]/div/button[1]/div/span")).click().click(); //查询更新
        Thread.sleep(1000);
        return true;
    }

    public ChromiumTab getTab() {
        return tab;
    }

    public void setTab(ChromiumTab tab) {
        this.tab = tab;
    }

}

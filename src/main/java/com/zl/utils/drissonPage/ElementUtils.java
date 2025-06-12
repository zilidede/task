package com.zl.utils.drissonPage;

import com.ll.drissonPage.base.By;
import com.ll.drissonPage.element.ChromiumElement;
import com.ll.drissonPage.functions.Keys;
import com.ll.drissonPage.page.ChromiumPage;
import com.ll.drissonPage.page.ChromiumTab;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// drissonPage框架对于的page tab 各元素情况
public class ElementUtils {

    public static void main(String[] args) throws InterruptedException {
        elementToTab();
    }

    public static ChromiumTab init() throws InterruptedException {
        ChromiumPage chromiumPage = ChromiumPage.getInstance("127.0.0.1:9223");
        chromiumPage.get("https://ad.xiaohongshu.com/microapp/traffic-guide/keywordsInsight");
        Thread.sleep(3000);
        ChromiumTab tab = chromiumPage.getTab();
        return tab;
    }

    public static String elementToTab() throws InterruptedException {
        String xpath = "";
        ChromiumTab tab = ElementUtils.init();

        ChromiumElement element = null;
        List<ChromiumElement> elements = null;
        int size = 0;
        // 查询搜索词搜索次数
        //
        String name = "";
        tab.get("https://ad.xiaohongshu.com/microapp/traffic-guide/keywordAnalysis");
        Thread.sleep(3000);
        xpath = "//*[@id=\"traffic-guide-container\"]/div/div[1]/div/div[1]/button/div/span[1]/span[2]";
        tab.ele(By.xpath(xpath)).click().click(); //打开选择框
        Thread.sleep(1000);
        element = tab.ele(By.xpath("//*[@class=\"d-text d-select-placeholder d-text-ellipsis d-text-nowrap\"]"));
        Thread.sleep(1000);
        element.input(name); //输入搜索词
        Thread.sleep(1000);
        tab.eles(By.xpath("//*[@class=\"d-option-name\"]")).get(0).click().click(); //选择第一行
        Thread.sleep(1000);
        xpath = "//*[@id=\"traffic-guide-container\"]/div/div[1]/div/div[1]/button/div/span[1]/span[2]";
        tab.ele(By.xpath(xpath)).click().click(); //关闭选项框
        Thread.sleep(1000);
        tab.ele(By.xpath("//*[@id=\"traffic-guide-container\"]/div/div[1]/div/button[1]/div/span")).click().click(); //查询更新
        Thread.sleep(1000);
        //聚光搜索词相关内容
        xpath = "//*[@class=\"d-table__body\"]";
        elements = tab.eles(By.xpath(xpath));
        elements = elements.get(1).eles(By.xpath("./tr"));
        int j = 0;
        size = elements.size();
        size = 0;
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
        //聚光搜索词下载；
        xpath = "//*[@class=\"d-new-cascader__content\"]";
        element = tab.ele(By.xpath(xpath));
        Thread.sleep(1000);
        element.click().click();
        Thread.sleep(1000);
        xpath = "//*[@class=\"d-new-cascader__column\"]";
        elements = tab.eles(By.xpath(xpath)).get(2).eles(By.xpath("./div"));
        size = elements.size();
        for (int i = 0; i < size; i++) {
            ChromiumElement e = elements.get(i);
            e.click().click();
            Thread.sleep(1000);
            System.out.println(e.text());
            //查询-下载
            xpath = "//*[@id=\"traffic-guide-container\"]/div/div[1]/div/button[1]/div/span";
            tab.ele(By.xpath(xpath)).click().click();
            Thread.sleep(1000 * 5);
            xpath = "//*[@class=\"d-button-content\"]";
            tab.eles(By.xpath(xpath)).get(12).click().click();
            Thread.sleep(1000 * 1);
            xpath = "//*[@class=\"d-new-cascader__content\"]";
            tab.ele(By.xpath(xpath)).click().click();
            Thread.sleep(1000 * 1);
            xpath = "//*[@class=\"d-new-cascader__column\"]";
            elements = tab.eles(By.xpath(xpath)).get(2).eles(By.xpath("./div"));
        }
        return "";

    }

}

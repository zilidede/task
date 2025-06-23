package com.zl.task.craw.keyword;

import com.ll.drissonPage.base.By;
import com.ll.drissonPage.element.ChromiumElement;
import com.zl.task.craw.base.CrawBaseXHR;
import com.zl.task.vo.task.taskResource.TaskVO;

import java.util.List;

//爬取巨量云图单个关键词详情
public class CrawSingleOceanEngineKeyword extends CrawBaseXHR {
    List<String> keywords;
    @Override
    public void run(TaskVO task) throws Exception {
        //爬取循环
        keywords = (List<String>) task.getTaskResource().getT();
        setListonXhr();
        craw();
        save();
        keywords.clear();
    }
    @Override
    public void craw() throws InterruptedException {
        openEnterUrl("https://yuntu.oceanengine.com/yuntu_lite/search_strategy/search_words", 10.0d);
        String xpath="//*[@class=\"search-strategy-cascader search-strategy-cascader-select search-strategy-can-input-grouped\"]" ;
        List<ChromiumElement> elements=getTab().eles(By.xpath(xpath)); //打开搜索词输入框
        elements.get(1).click().click();
        Thread.sleep(1000);
        for (String keyword : keywords) {
            xpath= "//*[@class=\"search-strategy-input search-strategy-input-size-md\"]";
            List<ChromiumElement> elements1=getTab().eles(By.xpath(xpath)); //输入搜索词
            Thread.sleep(1000);
            ChromiumElement element=elements1.get(5);
            element.input( keyword);
            Thread.sleep(2000);
            xpath= "//*[@class=\"search-strategy-list-item-inner-wrapper search-strategy-cascader-item-inner-wrapper\"]";;
            elements1=getTab().eles(By.xpath(xpath)); //选择搜索框
            elements1.get(0).click().click();
            for (ChromiumElement element1 : elements1){
                String s=element1.text();
               // System.out.println(s);
            }
            Thread.sleep(2000);
            //重新打开搜索框；
            xpath="//*[@class=\"search-strategy-cascader search-strategy-cascader-select search-strategy-can-input-grouped\"]" ;
            elements=getTab().eles(By.xpath(xpath)); //打开搜索词输入框
            elements.get(1).click().click();
        }
    }

}

package com.zl.task.craw.keyword;

import com.ll.drissonPage.base.By;
import com.ll.drissonPage.element.ChromiumElement;
import com.ll.drissonPage.page.ChromiumTab;
import com.zl.task.craw.base.CrawBaseXHR;
import com.zl.task.vo.task.TaskVO;
import com.zl.utils.other.Ini4jUtils;

import java.util.ArrayList;
import java.util.List;


// 爬取抖音网页榜搜索词详情
public class CrawDouYinWebKeywords extends CrawBaseXHR  {
    private ChromiumTab  tab;
    private List<String>  keywords;
    CrawDouYinWebKeywords(ChromiumTab  tab) throws Exception {
        this.tab = tab;
        Ini4jUtils.loadIni("./data/config/config.ini");
        Ini4jUtils.setSectionValue("douYinWeb");
        setXhrSaveDir(Ini4jUtils.readIni("xhrDir"));
        Ini4jUtils.loadIni("./data/task/xhr.ini");
        setXhrList(Ini4jUtils.traSpecificSection("douYinWeb"));
    }
    @Override
    public void run(TaskVO task) throws Exception {
        //爬取循环
        setListonXhr();
        List<String> keywords = (List<String>) task.getTaskResource();
        crawRelatedKeywords( keywords);
        save();
    }
   public  List<String>  crawRelatedKeywords(List<String> keywords) throws Exception{
       List<String> list = new ArrayList<>();
       tab.get("https://www.douyin.com/");
       Thread.sleep(1000 * 5);
       String xpath = "//*[@id=\"douyin-header\"]/div[1]/header/div/div/div[1]/div/div[2]/div/div[1]/input";
       ChromiumElement element=tab.ele(By.xpath(xpath));
       for(String keyword:keywords){
           element.input(keyword);
           Thread.sleep(1000 * 2);
       }
       return list;

   }

}

package com.zl.task.craw.keyword;

import com.ll.drissonPage.base.By;
import com.ll.drissonPage.element.ChromiumElement;
import com.ll.drissonPage.page.ChromiumTab;
import com.zl.task.craw.base.CrawBaseXHR;
import com.zl.task.vo.task.taskResource.TaskVO;
import com.zl.utils.other.Ini4jUtils;

import java.util.ArrayList;
import java.util.List;


// 爬取抖音网页榜搜索词详情
public class CrawDouYinWebKeywords extends CrawBaseXHR  {
    private List<String>  keywords;
    public CrawDouYinWebKeywords(ChromiumTab tab) throws Exception {
        super(tab);
        Ini4jUtils.loadIni("./data/config/config.ini");
        Ini4jUtils.setSectionValue("douYinWeb");
        setXhrSaveDir(Ini4jUtils.readIni("xhrDir"));
        Ini4jUtils.loadIni("./data/task/xhr.ini");
        setXhrList(Ini4jUtils.traSpecificSection("douYinWeb"));
        tab.listen().start(getXhrList());
    }
    @Override
    public void run(TaskVO task) throws Exception {
        //爬取循环
        List<String> keywords = (List<String>) task.getTaskResource().getT();
        getTab().listen().start(getXhrList());
        crawRelatedKeywords(keywords);


    }

    @Override
    public void craw() throws InterruptedException {

    }

    public  List<String>  crawRelatedKeywords(List<String> keywords) throws Exception{
       List<String> list = new ArrayList<>();
       getTab().get("https://www.douyin.com/");
       Thread.sleep(1000 * 5);
       String xpath = "//*[@id=\"douyin-header\"]/div[1]/header/div/div/div[1]/div/div[2]/div/div[1]/input";
       ChromiumElement element=getTab().ele(By.xpath(xpath));
       int i=0;
       for(String keyword:keywords){
           element.input(keyword);
           Thread.sleep(1000 * 2);
           if(i++>100){
               i=0;
               save();

           }
       }
       save();
       return list;

   }

}

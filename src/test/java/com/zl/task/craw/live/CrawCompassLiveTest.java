package com.zl.task.craw.live;

import com.zl.task.craw.list.CrawDouYinShopCompassHourLive1;
import com.zl.task.vo.task.taskResource.DefaultTaskResourceCrawTabList;
import com.zl.task.vo.task.taskResource.TaskVO;
import org.junit.Before;
import org.junit.Test;


public  class CrawCompassLiveTest {
    CrawDouYinShopCompassHourLive1 crawler;
    @Before
    public void setUp() throws Exception {
        crawler = new CrawDouYinShopCompassHourLive1();
        crawler.setTab(DefaultTaskResourceCrawTabList.getTabList().get(1));
        crawler.openEnterUrl("https://compass.jinritemai.com/shop/chance/live-rank?from_page=%2Fshop%2Flive-list");
    }

    @org.junit.Test
    public void run() throws Exception {
        TaskVO task = new TaskVO(1,"");
        task.setTaskDesc("小时榜&7:00&运动户外");
        crawler.run(task);
    }

    @org.junit.Test
    public void crawLiveHourList() {
    }

    @org.junit.Test
    public void craw() throws Exception {
        crawler.craw(null);
    }

    @org.junit.Test
    public  void name() {
    }

    @org.junit.Test
    public  void crawCompassListOne() {
    }

    @org.junit.Test
   public void selectTime() throws InterruptedException {
       crawler.selectTime("14:00","小时榜");
    }

    @org.junit.Test
    public  void selectCategory() throws InterruptedException {
        crawler.selectCategory("珠宝文玩");
    }


    @Test
    public void crawCompassList() throws Exception {
        //翻页操作
        crawler.crawCompassList();
    }

    @org.junit.Test
    public void isCraw() {
        crawler.isCraw(30*30l) ;
    }
}
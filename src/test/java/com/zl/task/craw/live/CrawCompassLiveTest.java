package com.zl.task.craw.live;

import com.zl.task.vo.task.taskResource.DefaultTaskResourceCrawTabList;
import com.zl.task.vo.task.taskResource.TaskVO;
import org.junit.Before;




public  class CrawCompassLiveTest {
    CrawCompassLive crawler;
    @Before
    public void setUp() throws Exception {
        crawler = new CrawCompassLive();
        crawler.setTab(DefaultTaskResourceCrawTabList.getTabList().get(1));
        crawler.openEnterUrl("https://compass.jinritemai.com/shop/chance/live-rank?from_page=%2Fshop%2Flive-list");
    }

    @org.junit.Test
    public void run() {
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


}
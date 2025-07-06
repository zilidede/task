package com.zl.task.craw.video;


import com.zl.task.vo.task.taskResource.DefaultTaskResourceCrawTabList;

import com.zl.task.vo.task.taskResource.TaskVO;
import org.junit.Before;
import org.junit.jupiter.api.Test;



public class CrawCompassVideoTest {
    private CrawCompassVideo crawler;
    @Before
    public void init() throws Exception {
        crawler = new CrawCompassVideo(DefaultTaskResourceCrawTabList.getTabList().get(2));
        crawler.openEnterUrl("https://compass.jinritemai.com/shop/chance/video-rank?from_page=%2Fshop%2Fvideo%2Foverview");
    }

    @Test
    void run() {
    }


    public void craw() throws Exception {
        TaskVO task = new TaskVO(1, "爬取抖店罗盘商品榜单");
        task.setTaskDesc("近一天&视频销量榜&电商&服饰内衣&2");
        crawler.craw(task);
    }



    @org.junit.Test
    public void selectTime() throws InterruptedException {
        crawler.selectTime("","近一天");
        crawler.selectTime("","近七天");
        crawler.selectTime("","近30天");
    }

    @org.junit.Test
    public void selectListType() throws InterruptedException {
        crawler.selectListType("视频销量榜");
        crawler.selectListType("引流直播榜");
        crawler.selectListType("热门视频榜");

    }
}
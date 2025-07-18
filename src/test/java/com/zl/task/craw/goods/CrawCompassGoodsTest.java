package com.zl.task.craw.goods;

import com.zl.task.craw.live.CrawCompassLive;
import com.zl.task.vo.task.taskResource.DefaultTaskResourceCrawTabList;
import com.zl.task.vo.task.taskResource.TaskVO;
import org.junit.Before;
import org.junit.jupiter.api.Test;


public class CrawCompassGoodsTest {
    private CrawCompassGoods crawler;
    @Before
    public void init() throws Exception {
        crawler = new CrawCompassGoods(DefaultTaskResourceCrawTabList.getTabList().get(2));
        crawler.openEnterUrl("https://compass.jinritemai.com/shop/chance/product-rank?from_page=%2Fshop%2Fcommodity%2Fproduct-list");
    }
    @Test
    void run() {

    }

    @org.junit.Test
    public  void craw() throws Exception {
        TaskVO task = new TaskVO(1, "爬取抖店罗盘商品榜单");
        task.setTaskDesc("近一天&总榜&电商&服饰内衣&2");
        crawler.craw(task);
    }

    @org.junit.Test
    public void selectTime() throws InterruptedException {
        crawler.selectTime("","近30天");

    }

    @org.junit.Test
    public void selectListType() throws InterruptedException {
        crawler.selectListType("总榜");
        crawler.selectListType("搜索榜");
        crawler.selectListType("直播榜");
        crawler.selectListType("短视频榜");
        crawler.selectListType("商品卡榜");
    }

}
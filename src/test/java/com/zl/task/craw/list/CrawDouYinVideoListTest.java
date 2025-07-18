package com.zl.task.craw.list;

import com.zl.task.vo.task.taskResource.DefaultTaskResourceCrawTabList;
import com.zl.task.vo.task.taskResource.TaskVO;
import org.junit.Before;

public class CrawDouYinVideoListTest {
    private CrawDouYinVideoList craw;

    @Before
    public void init() throws Exception {
        //测试webdriver；
        craw = new CrawDouYinVideoList();
        craw.setTab(DefaultTaskResourceCrawTabList.getTabList().get(0));
        craw.setListonXhr();
        String url = "https://compass.jinritemai.com/shop/chance/video-rank?from_page=%2Fshop%2Fshort-video-analysis";
        craw.openEnterUrl(url);
    }

    @org.junit.Test
    public void select() throws InterruptedException {
        TaskVO taskVO = new TaskVO(1, "抖店罗盘视频榜单");
        taskVO.setTaskDesc("近一天&引流直播榜&生鲜-6");
        craw.select(taskVO);
    }

    @org.junit.Test
    public void craw() throws Exception {
        TaskVO taskVO = new TaskVO(1, "抖店罗盘视频销量榜");
        taskVO.setTaskDesc("近一天&引流直播榜&服饰内衣-2");
        craw.select(taskVO);
        craw.craw(taskVO);
    }


}
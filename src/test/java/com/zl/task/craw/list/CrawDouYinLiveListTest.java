package com.zl.task.craw.list;

import com.zl.task.impl.taskResource.DefaultTaskResourceCrawTabList;
import com.zl.task.vo.task.TaskVO;
import org.junit.Before;
import org.junit.Test;

public class CrawDouYinLiveListTest {
    private CrawDouYinLiveList craw;

    @Before
    public void init() throws Exception {
        //测试webdriver；
        craw = new CrawDouYinLiveList();
        craw.setTab(DefaultTaskResourceCrawTabList.getTabList().get(0));
        craw.setListonXhr();
        String url = "https://compass.jinritemai.com/shop/chance/live-rank?from_page=%2Fshop%2Flive-list";
        craw.openEnterUrl(url);
    }

    @org.junit.Test
    public void select() throws InterruptedException {
        TaskVO taskVO = new TaskVO(1, "抖店罗盘直播榜单");
        taskVO.setTaskDesc("小时榜-13:00&直播交易榜&服饰内衣-2");
        craw.select(taskVO);

    }

    @Test
    public void craw() throws Exception {
        TaskVO taskVO = new TaskVO(1, "抖店罗盘直播榜单");
        taskVO.setTaskDesc("小时榜-13:00&直播交易榜&服饰内衣-2");
        craw.select(taskVO);
        craw.craw(taskVO);
    }


}
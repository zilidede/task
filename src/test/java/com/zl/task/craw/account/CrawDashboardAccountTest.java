package com.zl.task.craw.account;

import com.zl.task.impl.taskResource.DefaultTaskResourceCrawTabList;
import com.zl.task.vo.task.AccountVO;
import org.junit.Before;
import org.junit.Test;

public class CrawDashboardAccountTest {
    private CrawDashboardAccount crawler;

    @Before
    public void setUp() throws Exception {
        crawler = new CrawDashboardAccount(DefaultTaskResourceCrawTabList.getTabList().get(0), "dashboard", "S:\\data\\task\\爬虫\\dashboard");
    }

    @Test
    public void craw() throws Exception {
        AccountVO vo = new AccountVO();
        vo.setAccountName("交个朋友直播间");
        crawler.craw(vo);

    }
}
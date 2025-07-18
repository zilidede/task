package com.zl.task.craw.account;


import com.ll.drissonPage.page.ChromiumTab;
import com.zl.dao.generate.HourLiveRankDO;
import com.zl.task.vo.task.taskResource.DefaultTaskResourceCrawTabList;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.io.IOException;

public class CrawHuiTunAccountTest {

    @Mock
    private ChromiumTab chromiumTab;

    @InjectMocks
    private CrawHuiTunAccount crawHuiTunAccount;

    @Before
    public void setUp() throws IOException {
        // MockitoAnnotations.initMocks(this);
        crawHuiTunAccount = new CrawHuiTunAccount(DefaultTaskResourceCrawTabList.getTabList().get(1), "huiTunLive", "S:\\data\\task\\爬虫\\huiTunLive");
    }

    @Test
    public void crawlLiveDetail_UrlOpenedAndXHRSaved() throws Exception {
        HourLiveRankDO hourLiveRankDO = new HourLiveRankDO();
        String url = "https://dy.huitun.com/app/#/app/analyze/analyze_search/live_detail?roomId=7470671713399802651&uid=4195355415549012";
        // 验证父类方法是否被调用
        crawHuiTunAccount.openUrl(url, 5.0);
        Thread.sleep(1000 * 5);
        url = "https://dy.huitun.com/app/#/app/anchor/anchor_list/anchor_detail?id=4195355415549012&tabKey=live_record";
        crawHuiTunAccount.openUrl(url, 5.0);
        Thread.sleep(1000 * 5);
        crawHuiTunAccount.saveXHR(DefaultTaskResourceCrawTabList.getTabList().get(1));
        // verify(crawHuiTunAccount).openUrl(url, 5.0);
        //  verify(crawHuiTunAccount).saveXHR();
    }

    @Test
    public void crawLiveRecord() throws InterruptedException {
        HourLiveRankDO vo = new HourLiveRankDO();
        vo.setAuthorId(4195355415549012L);
        crawHuiTunAccount.crawLiveRecord(vo, 10);
    }

    @Test
    public void crawVideoRecord() throws InterruptedException {
        HourLiveRankDO vo = new HourLiveRankDO();
        vo.setAuthorId(4195355415549012L);
        crawHuiTunAccount.crawVideoRecord(vo, 10);
    }
}

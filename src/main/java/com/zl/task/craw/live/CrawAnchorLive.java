package com.zl.task.craw.live;

import com.ll.drissonPage.base.By;
import com.ll.drissonPage.element.ChromiumElement;
import com.ll.drissonPage.page.ChromiumTab;
import com.zl.task.craw.SaveXHR;
import com.zl.task.craw.base.CrawBaseXHR;

import java.io.IOException;

// 抖音灵机抓取
public class CrawAnchorLive extends CrawBaseXHR {
    public CrawAnchorLive(ChromiumTab tab) throws Exception {
        super(tab);
        super.init("live");
    }

    @Override
    public void craw() throws InterruptedException {
        getTab().listen().start(getXhrList());
        openEnterUrl("https://anchor.douyin.com/anchor/dashboard?type=0&roomId=&from=lingji_redirect");
        Thread.sleep(4000);
        String xpath="//*[@id=\"root\"]/div/div[2]/div[2]/div/div[1]/div[1]/div[1]/div[2]/div";
        // 添加显示等待
        ChromiumElement element= getTab().ele(By.xpath(xpath));
        element.click().click();
        SaveXHR.saveXhr(getTab(), getXhrSaveDir(), getXhrList());



    }
}

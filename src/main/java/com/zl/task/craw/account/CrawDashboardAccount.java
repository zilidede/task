package com.zl.task.craw.account;

import com.ll.drissonPage.base.By;
import com.ll.drissonPage.element.ChromiumElement;
import com.ll.drissonPage.page.ChromiumTab;
import com.zl.task.craw.base.CrawServiceXHRTabImpl;
import com.zl.task.vo.task.AccountVO;

import java.io.IOException;
import java.util.List;

// 达人广场
public class CrawDashboardAccount extends CrawServiceXHRTabImpl {


    /**
     * 构造函数
     *
     * @param tab        浏览器标签页对象
     * @param xhr        xhr名称
     * @param xhrSaveDir xhr保存目录
     * @throws IOException 当初始化XHR监听失败时抛出
     */
    public CrawDashboardAccount(ChromiumTab tab, String xhr, String xhrSaveDir) throws IOException {
        super(tab, xhr, xhrSaveDir);
    }

    public void craw(AccountVO vo) throws Exception {
        String url = "https://buyin.jinritemai.com/dashboard/servicehall/daren-square?previous_page_name=3&previous_page_type=4";
        openUrl(url, 5.0);
        Thread.sleep(1000 * 2);
        ChromiumTab tab1 = search(vo.getAccountName());//搜素达人主链接；
        listenXHR(tab1);
        Thread.sleep(1000 * 2);
        crawAccountPage(tab1);
        super.saveXHR(tab1);

    }

    public void crawAccountPage(ChromiumTab tab) throws InterruptedException {
        //
        String xpath = "//*[@class=\"auxo-tabs-nav-list\"]/div";
        ChromiumElement element = tab.eles(By.xpath(xpath)).get(1);
        Thread.sleep(1000);
        String s = element.text();
        element.click().click(); //场景
        Thread.sleep(1000);
        xpath = "//*[@class=\"auxo-checkbox-input\"]";
        Thread.sleep(1000);
        tab.eles(By.xpath(xpath)).get(0).click().click(); //去除带货标识
        Thread.sleep(1000);
        xpath = "//*[@class=\"auxo-radio-group auxo-radio-group-outline\"]";
        List<ChromiumElement> elements = tab.eles(By.xpath(xpath));
        Thread.sleep(1000);
        elements = elements.get(1).eles(By.xpath("./label"));
        Thread.sleep(1000);
        s = elements.get(1).text();
        element = elements.get(1).ele(By.xpath("./span[2]"));
        element.runAsyncJs("arguments[0].click();");
        // element.click().click(); //视频xhr
        Thread.sleep(1000);
        element = elements.get(0).ele(By.xpath("./span[2]"));
        element.click().click(); //直播xhr
        Thread.sleep(1000);


    }

    public ChromiumTab search(String keyword) throws Exception {
        String xpath = "//*[@placeholder=\"请输入达人昵称或抖音号\"]";
        ChromiumElement element = getTab().ele(By.xpath(xpath));
        element.input(keyword); //输入搜索内容
        Thread.sleep(1000);
        xpath = "//*[@class=\"auxo-btn auxo-btn-primary auxo-input-search-button\"]";
        getTab().ele(By.xpath(xpath)).click().click();
        Thread.sleep(2000);
        xpath = "//*[@class=\"auxo-dorami-info-card-title-content\"]";
        element = getTab().eles(By.xpath(xpath)).get(0); //点击搜索结果第一行
        Thread.sleep(1000);
        String s = element.text();
        // element.click().click();
        Thread.sleep(1000);
        return element.click().forNewTab();
    }
}

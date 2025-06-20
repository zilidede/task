package com.zl.task.craw.account;

import com.ll.drissonPage.base.By;
import com.ll.drissonPage.element.ChromiumElement;
import com.ll.drissonPage.page.ChromiumTab;
import com.zl.dao.generate.HourLiveRankDO;
import com.zl.task.craw.base.CrawServiceXHRTabImpl;

import java.io.IOException;
import java.util.List;

public class CrawHuiTunAccount extends CrawServiceXHRTabImpl {

    /**
     * 构造函数
     *
     * @param tab        浏览器标签页对象
     * @param xhr        xhr名称
     * @param xhrSaveDir xhr保存目录
     * @throws IOException 当初始化XHR监听失败时抛出
     */
    public CrawHuiTunAccount(ChromiumTab tab, String xhr, String xhrSaveDir) throws IOException {
        super(tab, xhr, xhrSaveDir);
    }

    public void run(HourLiveRankDO vo, int count) throws Exception {
        crawlLiveDetail(vo);
        crawLiveRecord(vo, count);
        //crawVideoRecord(vo,count*2);
    }

    /**
     * 根据给定的直播房间信息抓取直播详情
     * 此方法通过打开特定的URL来抓取直播详情页面，以获取更详细的数据
     *
     * @param vo 包含直播房间和作者信息的数据对象，用于构造请求URL
     * @throws Exception 如果在抓取过程中发生错误，抛出此异常
     */
    public void crawlLiveDetail(HourLiveRankDO vo) throws Exception {
        // 构造直播详情的URL，包含房间ID和作者ID
        String url = String.format("https://dy.huitun.com/app/#/app/analyze/analyze_search/live_detail?roomId=%d&uid=%d", vo.getRoomId(), vo.getAuthorId());

        // 打开构造的URL，尝试加载直播详情页面
        super.openUrl(url, 5.0);

        // 等待3秒，以确保页面完全加载，避免因加载不完全导致的数据抓取不完整
        Thread.sleep(1000 * 3);

        // Your implementation here
    }

    /**
     * 爬取直播记录
     * 该方法负责打开指定的直播详细页面，并通过滚动页面来加载更多的直播记录，最后保存XHR请求
     *
     * @param vo    包含作者ID的HourLiveRankDO对象，用于构造直播详情页的URL
     * @param count 需要滚动页面的次数，以加载更多的直播记录
     * @throws InterruptedException 当线程睡眠被中断时抛出此异常
     */
    public void crawLiveRecord(HourLiveRankDO vo, int count) throws InterruptedException {
        // 根据作者ID构造直播详情页的URL
        String url = String.format("https://dy.huitun.com/app/#/app/anchor/anchor_list/anchor_detail?id=%d&tabKey=live_record", vo.getAuthorId());
        // 打开构造好的URL，并设置超时时间为5秒
        openUrl(url, 5.0);
        // 线程睡眠3秒，等待页面加载完成
        Thread.sleep(1000 * 3);
        // 定义XPath表达式，用于获取页面上的标签页元素
        String xpath = "//*[@class=\"ant-radio-button-wrapper\"]";
        // 获取所有符合XPath表达式的元素，并存储在列表中
        List<ChromiumElement> elementList = getTab().eles(By.xpath(xpath));
        // 点击第四个标签页，通常是“直播记录”标签
        //   elementList.get(3).click().click();
        // 定义JavaScript代码，用于滚动页面到底部
        String js = "window.scrollTo(0, document.body.scrollHeight)";
        // 循环执行滚动操作和保存XHR请求，次数由参数count指定
        for (int i = 0; i < count; i++) {
            // 执行JavaScript代码，滚动页面到底部
            getTab().runAsyncJs(js);
            // 线程睡眠4秒，等待新内容加载
            Thread.sleep(1000 * 4);
            // 保存XHR请求，以获取加载更多内容后的数据
            saveXHR(getTab());
        }
    }

    public void crawVideoRecord(HourLiveRankDO vo, int count) throws InterruptedException {
        // 根据作者ID构造直播详情页的URL
        String url = String.format("https://dy.huitun.com/app/#/app/anchor/anchor_list/anchor_detail?id=%d&tabKey=blogger_video", vo.getAuthorId());
        // 打开构造好的URL，并设置超时时间为5秒
        openUrl(url, 5.0);
        // 线程睡眠3秒，等待页面加载完成
        Thread.sleep(1000);
        String xpath = "//*[@class=\"ant-tabs-nav ant-tabs-nav-animated\"]/div[1]/div";
        getTab().eles(By.xpath(xpath)).get(2).click().click();
        Thread.sleep(1000);
        // 定义XPath表达式，用于获取页面上的标签页元素
        xpath = "//*[@class=\"ant-radio-button-wrapper\"]";
        // 获取所有符合XPath表达式的元素，并存储在列表中
        List<ChromiumElement> elementList = getTab().eles(By.xpath(xpath));
        // 点击第四个标签页，通常是“直播记录”标签
        //   elementList.get(3).click().click();
        // 定义JavaScript代码，用于滚动页面到底部
        String js = "window.scrollTo(0, document.body.scrollHeight)";
        // 循环执行滚动操作和保存XHR请求，次数由参数count指定
        for (int i = 0; i < count; i++) {
            // 执行JavaScript代码，滚动页面到底部
            getTab().runAsyncJs(js);
            // 线程睡眠4秒，等待新内容加载
            Thread.sleep(1000 * 2);
            // 保存XHR请求，以获取加载更多内容后的数据
            saveXHR(getTab());
        }
    }


}
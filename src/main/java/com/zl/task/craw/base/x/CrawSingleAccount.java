package com.zl.task.craw.base.x;

import com.zl.task.vo.task.AccountVO;

//爬取单一账号信息
public class CrawSingleAccount {
    public static void craw(AccountVO vo) throws Exception {
        //		c. CrawDashboardAccount类-爬取达人广场账号
        //		d. CrawDouHotAccount类-爬取热点宝账号
        //		e. CrawHuitunAccount类-爬取灰豚数据账号
        //		f. CrawDouYinAccount类-爬取抖音。
        vo.setAccountName("不辣嫂子");
        //CrawDashboardAccount crawler = new CrawDashboardAccount(DefaultTaskResourceCrawTabList.getTabList().get(0));
        //crawler.craw(vo);
    }

}

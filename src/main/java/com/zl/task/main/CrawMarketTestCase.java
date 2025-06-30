package com.zl.task.main;

import com.zl.task.craw.market.CrawSeleniumDouYinCategoryList;
import com.zl.task.vo.task.taskResource.DefaultTaskResourceCrawTabList;
import com.zl.task.vo.task.taskResource.TaskVO;

//爬取市场大盘
public class CrawMarketTestCase {
    public static void main(String[] args) throws Exception {
        crawSingleAllMarketCategory();
    }
    public static void crawSingleAllMarketCategory() throws Exception {
        CrawSeleniumDouYinCategoryList crawler = null;
        try {
            crawler = new CrawSeleniumDouYinCategoryList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        crawler.setTab(DefaultTaskResourceCrawTabList.getTabList().get(0));
        try {
            TaskVO taskVO=new TaskVO(1, "抖店罗盘类目榜单");
            taskVO.setTaskDesc("服饰内衣");
            crawler.run(taskVO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

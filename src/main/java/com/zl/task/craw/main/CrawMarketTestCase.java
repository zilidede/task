package com.zl.task.craw.main;

import cn.hutool.log.Log;
import com.zl.task.craw.market.CrawSeleniumDouYinCategoryList;
import com.zl.task.vo.task.taskResource.DefaultTaskResourceCrawTabList;
import com.zl.task.vo.task.taskResource.TaskVO;
import com.zl.utils.io.FileIoUtils;
import com.zl.utils.log.LoggerUtils;

//爬取市场大盘
public class CrawMarketTestCase {
    public static void main(String[] args) throws Exception {
        crawAllMarketCategoryFromFile();
    }
    public static void crawAllMarketCategoryFromFile() throws Exception {
        LoggerUtils.logger.debug("初始化CrawSeleniumDouYinCategoryLis对象");
        CrawSeleniumDouYinCategoryList crawler = null;
        try {
            crawler = new CrawSeleniumDouYinCategoryList();
        } catch (Exception e) {
            LoggerUtils.logger.debug("初始化CrawSeleniumDouYinCategoryLis对象失败");
            throw new RuntimeException(e);
        }
        crawler.setTab(DefaultTaskResourceCrawTabList.getTabList().get(0));
        LoggerUtils.logger.info("开始爬取市场大盘:");
        String [] categorys=FileIoUtils.readFile("./data/task/抖店市场大盘类目").split("\r\n");
        for(String category:categorys){
            LoggerUtils.logger.info("爬取市场大盘:"+category);
            crawMarketAppointCategory(crawler,category);
            LoggerUtils.logger.info("爬取市场大盘成功:"+category);
        }
    }
    //爬取指定类目
    public static void crawMarketAppointCategory( CrawSeleniumDouYinCategoryList crawler,String categoryName) throws Exception {
        try {
            TaskVO taskVO=new TaskVO(1, "抖店罗盘类目榜单");
            taskVO.setTaskDesc(categoryName);
            crawler.run(taskVO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}

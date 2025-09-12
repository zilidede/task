package com.zl.task.craw.category;

import com.zl.task.craw.keyword.CrawTrendinsightKeywords;
import com.zl.task.vo.task.taskResource.DefaultTaskResourceCrawTabList;
import com.zl.utils.io.FileIoUtils;

//爬取巨量算数测试用例
public class CrawTrendinsightTestCase {
    public static void main(String[] args) throws Exception {
        crawlTrendInsightKeywordWithTimeRanges();
    }

    //爬取巨量算数词根指定时间段词根指数
    public static void crawlTrendInsightKeywordWithTimeRanges() throws Exception {
        CrawTrendinsightKeywords crawler = new CrawTrendinsightKeywords();
        crawler.setTab(DefaultTaskResourceCrawTabList.getTabList().get(1));
        crawler.getTab().listen().start(crawler.getXHRList());
        String[] keywords = FileIoUtils.readTxtFile("./data/task/手工词根列表.txt","utf-8").split("\r\n");
        String startTime = "2022-01-01";
        String endTime = "2025-06-17";
        for (String s : keywords) {
            crawler.craw(s,"",  startTime, endTime);
           // break;
        }
    }
}

package com.zl.task.craw.keyword;

import com.zl.task.vo.task.taskResource.DefaultTaskResourceCrawTabList;
import com.zl.task.vo.task.taskResource.ListResource;
import com.zl.task.vo.task.taskResource.TaskResource;
import com.zl.task.vo.task.taskResource.TaskVO;


import java.util.ArrayList;
import java.util.List;

public class CrawDouYinWebKeywordsTest {

    @org.junit.Test
    public void crawRelatedKeywords() throws Exception {
        List<String> keywords= new ArrayList<>();
        keywords.add("街舞");
        keywords.add("防晒");
        keywords.add("裤子");
        CrawDouYinWebKeywords crawler = new CrawDouYinWebKeywords(DefaultTaskResourceCrawTabList.getTabList().get(0));
        crawler.crawRelatedKeywords(keywords);
    }

    @org.junit.Test
    public void run() throws Exception {
        List<String> keywords= new ArrayList<>();
        keywords.add("街舞");
        keywords.add("防晒");
        keywords.add("裤子");
        TaskResource<List<String>>listRes=new ListResource();
        listRes.load(keywords);
        CrawDouYinWebKeywords crawler = new CrawDouYinWebKeywords(DefaultTaskResourceCrawTabList.getTabList().get(0));

        TaskVO<List<String>>  task=new TaskVO(1,"爬取抖音网页搜索词下拉框",listRes);
        crawler.setTab(DefaultTaskResourceCrawTabList.getTabList().get(0));
        crawler.run(task);
    }
}
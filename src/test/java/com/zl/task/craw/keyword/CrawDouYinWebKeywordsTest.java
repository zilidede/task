package com.zl.task.craw.keyword;

import com.zl.task.impl.taskResource.DefaultTaskResourceCrawTabList;
import com.zl.task.impl.taskResource.TaskResource;
import com.zl.task.vo.task.TaskVO;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class CrawDouYinWebKeywordsTest {

    @Test
    void crawRelatedKeywords() throws Exception {
        List<String> keywords= new ArrayList<>();
        keywords.add("街舞");
        keywords.add("防晒");
        keywords.add("裤子");
        CrawDouYinWebKeywords crawler = new CrawDouYinWebKeywords(DefaultTaskResourceCrawTabList.getTabList().get(0));
        crawler.crawRelatedKeywords(keywords);
    }

    @Test
    void run() throws Exception {
        CrawDouYinWebKeywords crawler = new CrawDouYinWebKeywords(DefaultTaskResourceCrawTabList.getTabList().get(0));
        TaskVO  task=new TaskVO(1,"爬取抖音网页搜索词下拉框");
        List<String> keywords= new ArrayList<>();
        keywords.add("街舞");
        keywords.add("防晒");
        keywords.add("裤子");
        task.setTaskResource((TaskResource) keywords);
        crawler.run(task);
    }
}
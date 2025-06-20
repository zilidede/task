package com.zl.task.process.keyword;

import com.zl.task.craw.keyword.CrawTrendinsightKeywords;
import com.zl.task.impl.taskResource.DefaultTaskResourceCrawTabList;
import com.zl.task.save.parser.trendinsight.ParserTrendInSightKeywords;
import com.zl.task.save.parser.trendinsight.SaveTrendInSightKeywords;
import com.zl.task.vo.http.HttpVO;
import com.zl.task.vo.other.GenericListContainerVO;
import com.zl.task.vo.task.TaskVO;

import java.util.*;

// 深度获取巨量算数关键字信息；
public class DeepTrendInSightKeywords {
    // 获取根关键字，爬取指定深度的子关键字；
    //获取根关键字，解析关联关键字 ；deepCount=以遍历关联关键字次数为爬取次数；搜索长尾词作为搜索词，在抖音搜索词框中搜索寻找关联词并记录 ;
    // 关联搜索词词组重新进行爬取热度，并放入巨量云图记录30天搜索词；
    public  static  void crawRootKeyword(String keyword ,Integer deepCount) throws Exception {
        CrawTrendinsightKeywords crawler = new CrawTrendinsightKeywords();
        crawler.setTab(DefaultTaskResourceCrawTabList.getTabList().get(0));

        ParserTrendInSightKeywords parser = new ParserTrendInSightKeywords(); ;
        int i=0;

        Map<String,Integer> unCrawKeywordsMaps = new HashMap<>(); // 未爬取的关键字map
        unCrawKeywordsMaps.put(keyword,0);
        Map<String,Integer> crawKeywordsMaps = new HashMap<>();  // 已爬取关键字map
        while (i<deepCount){
            // 先获取当前键的静态视图（避免并发修改）
            Set<Map.Entry<String, Integer>> entries = unCrawKeywordsMaps.entrySet();
            List<String> toAddKeys = new ArrayList<>(); // 存储待添加的键
            for (Map.Entry<String, Integer> entry : entries) {
                String key = entry.getKey();
                if (!crawKeywordsMaps.containsKey(key)) {
                    List<HttpVO> httpVOS = crawler.craw(key, "", "");
                    crawKeywordsMaps.put(key, 0);
                    List<String> newKeys = getRelationKeywords(httpVOS);
                    toAddKeys.addAll(newKeys); // 收集待添加的键
                }
            }
            // 遍历结束后批量添加新键（避免并发修改）
            for (String newKey : toAddKeys) {
                unCrawKeywordsMaps.putIfAbsent(newKey, 0); // 避免重复添加
            }
            i++;
        }



    }
    public static List<String> getRelationKeywords(List<HttpVO> httpVOS) throws Exception {
        ParserTrendInSightKeywords parser = new ParserTrendInSightKeywords(); ;
        List<String> relationKeywords = new ArrayList<>();
        for (HttpVO httpVO : httpVOS) {
            parser.parserUrl(httpVO.getUrl());
            parser.parserJson(httpVO.getResponse().getBody());
            GenericListContainerVO container = parser.getContainer();
            //SaveTrendInSightKeywords.save( container);
        }
        return  relationKeywords;
    }

    // 爬取单个关键字巨量云图详情
    public static void crawSingleKeyWordToYunTn(String keyword) throws Exception {

    }
    // 爬取单个关键字巨量抖音网页版详情
    public static void crawSingleKeyWordToDouYinWeb(String keyword, String startTime, String endTime) throws Exception {

    }


}

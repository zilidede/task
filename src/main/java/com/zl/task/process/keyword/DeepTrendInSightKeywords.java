package com.zl.task.process.keyword;

import com.zl.task.craw.keyword.CrawDouYinWebKeywords;
import com.zl.task.craw.keyword.CrawSeleniumOceanEngineKeyWords;
import com.zl.task.craw.keyword.CrawTrendinsightKeywords;
import com.zl.task.save.Saver;
import com.zl.task.vo.task.taskResource.DefaultTaskResourceCrawTabList;
import com.zl.task.craw.keyword.ParserTrendInSightRelKeywords;
import com.zl.task.vo.http.HttpVO;
import com.zl.task.vo.task.taskResource.ListResource;
import com.zl.task.vo.task.taskResource.TaskResource;
import com.zl.task.vo.task.taskResource.TaskVO;
import com.zl.utils.log.LoggerUtils;
import com.zl.utils.other.Ini4jUtils;

import java.util.*;

// 深度获取巨量算数关键字信息；
public class DeepTrendInSightKeywords {
    // 获取根关键字，爬取指定深度的子关键字；
    //获取根关键字，解析关联关键字 ；deepCount=以遍历关联关键字次数为爬取次数；搜索长尾词作为搜索词，在抖音搜索词框中搜索寻找关联词并记录 ;
    // 关联搜索词词组重新进行爬取热度，并放入巨量云图记录30天搜索词；
    public  static  void crawRootKeyword(String keyword ,Integer deepCount) throws Exception {
        CrawTrendinsightKeywords crawler = new CrawTrendinsightKeywords();
        crawler.setTab(DefaultTaskResourceCrawTabList.getTabList().get(0));
        crawler.getTab().listen().start(crawler.getXHRList());
        int i=0;
        Map<String,Integer> unCrawKeywordsMaps = new HashMap<>(); // 未爬取的关键字map
        unCrawKeywordsMaps.put(keyword,0);
        Map<String,Integer> crawKeywordsMaps = new HashMap<>();  // 已爬取关键字map
        Ini4jUtils.loadIni("./data/config/config.ini");
        Ini4jUtils.setSectionValue("trendinsight");
        String xhrSaveDir = Ini4jUtils.readIni("xhrSaveDir");
        ParserTrendInSightRelKeywords parser = new ParserTrendInSightRelKeywords(); ;
        while (i<deepCount){
            // 先获取当前键的静态视图（避免并发修改）
            Set<Map.Entry<String, Integer>> entries = unCrawKeywordsMaps.entrySet();
            List<String> toAddKeys = new ArrayList<>(); // 存储待添加的键
            for (Map.Entry<String, Integer> entry : entries) {
                String key = entry.getKey();
                if (!crawKeywordsMaps.containsKey(key)) {
                    List<HttpVO> httpVOS;
                    try {
                       httpVOS = crawler.craw(key, "", "", "");
                    }
                    catch (Exception e){
                        e.printStackTrace();
                        LoggerUtils.logger.debug("爬取错误：" + key);
                        continue;
                    }

                    crawKeywordsMaps.put(key, 0);
                    List<String> newKeys = getRelationKeywords(parser,httpVOS);
                    toAddKeys.addAll(newKeys); // 收集待添加的键
                }
            }

            // 使用 HashSet 去重
            Set<String> set = new HashSet<>(toAddKeys);
            List<String> uniqueList = new ArrayList<>(set);
            //抖音网页版爬取;
            CrawDouYinWebKeywords crawlerDouYinWeb = new CrawDouYinWebKeywords(DefaultTaskResourceCrawTabList.getTabList().get(0));
            TaskResource<List<String>> taskResource=new ListResource();
            taskResource.load(uniqueList);
            TaskVO<List<String>> task=new TaskVO<>(1,"抖音网页版爬取",taskResource);
            crawlerDouYinWeb.run(task);
            // 爬取巨量云图单个搜索词;
            CrawSeleniumOceanEngineKeyWords crawlerSingleOceanEngineKeyword = new CrawSeleniumOceanEngineKeyWords();
            crawlerSingleOceanEngineKeyword.setTab(DefaultTaskResourceCrawTabList.getTabList().get(1));
            taskResource=new ListResource();
            taskResource.load(uniqueList);
            task=new TaskVO<>(2,"爬取巨量云图单个搜索词",taskResource);
            crawlerSingleOceanEngineKeyword.crawSingleKeywords(uniqueList);
            // 遍历结束后批量添加新键（避免并发修改）
            for (String newKey : toAddKeys) {
                unCrawKeywordsMaps.putIfAbsent(newKey, 0); // 避免重复添加
            }
            i++;
        }
        Saver.save();
    }
    public static List<String> getRelationKeywords(ParserTrendInSightRelKeywords parser, List<HttpVO> httpVOS) throws Exception {
        // 只解析关联词
        List<String> relationKeywords = new ArrayList<>();
        for (HttpVO httpVO : httpVOS) {
            List<String> result = parser.parser(httpVO); // 假设 parser 的返回值是 List<String>
            if (result != null && !result.isEmpty()) {
                relationKeywords.addAll(result); // 合并进总结果
            }
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

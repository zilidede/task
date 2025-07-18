package com.zl.task.main;

import com.zl.task.craw.keyword.CrawSeleniumOceanEngineKeyWords;
import com.zl.task.vo.task.taskResource.DefaultTaskResourceCrawTabList;
import com.zl.task.vo.task.taskResource.ListResource;
import com.zl.task.vo.task.taskResource.TaskResource;
import com.zl.task.vo.task.taskResource.TaskVO;
import com.zl.utils.log.LoggerUtils;

import java.util.ArrayList;
import java.util.List;
// 此测试用例用于云图搜索词洞察页爬取函数；
public class CrawSearchKeyWordTestCase {
   public static void main(String[] args) throws Exception {
       crawYunTuAllCategorySearchKeyWord();
   }
   //爬取云图行业指定行业搜索关键词详情
   public static void crawYunTuCategorySecondSearchKeyWord() throws Exception {
       //chromium浏览器标签页，用于爬虫操作
       CrawSeleniumOceanEngineKeyWords crawler;
       String categoryFilePath= "./data/task/云图行业.txt";
       try {
           crawler = new CrawSeleniumOceanEngineKeyWords();
           crawler.setTab(DefaultTaskResourceCrawTabList.getTabList().get(0));
           crawler.setFlag(true);  // 开启详情词爬取
           crawler.setSecondFlag(true); //开启二级类目爬取
       } catch (Exception e) {
           // 记录日志并重新抛出
           LoggerUtils.logger.error("初始化爬虫失败: " + e.getMessage());
           //  System.err.println("初始化爬虫失败: " + e.getMessage());
           throw new RuntimeException("初始化爬虫失败", e);
       }
       CrawSearchKeyword.crawYunTuSearchKeyword(crawler,categoryFilePath);
   }
    //爬取全部云图行业搜索关键词csv
   public static void crawYunTuAllCategorySearchKeyWord() throws Exception {
       //chromium浏览器标签页，用于爬虫操作
       CrawSeleniumOceanEngineKeyWords crawler;
       String categoryFilePath= "./data/task/全部云图行业.txt";
       try {
           crawler = new CrawSeleniumOceanEngineKeyWords();
           crawler.setTab(DefaultTaskResourceCrawTabList.getTabList().get(0));
           crawler.setFlag(false);
           crawler.setSecondFlag(false);
       } catch (Exception e) {
           // 记录日志并重新抛出
           LoggerUtils.logger.error("初始化爬虫失败: " + e.getMessage());
           //  System.err.println("初始化爬虫失败: " + e.getMessage());
           throw new RuntimeException("初始化爬虫失败", e);
       }
       CrawSearchKeyword.crawYunTuSearchKeyword(crawler,categoryFilePath);
   }
   //爬取云图行业搜索关键词相关图谱
   public static void crawYunTuRelaKeywords() throws Exception {
       List<String> uniqueList = new ArrayList<>();
       TaskResource<List<String>> taskResource=new ListResource();
       taskResource.load(uniqueList);
       TaskVO<List<String>> task=new TaskVO<>(1,"抖音网页版爬取",taskResource);
       // crawlerDouYinWeb.run(task);
       // 爬取巨量云图单个搜索词;
       CrawSeleniumOceanEngineKeyWords crawlerSingleOceanEngineKeyword = new CrawSeleniumOceanEngineKeyWords();
       crawlerSingleOceanEngineKeyword.setTab(DefaultTaskResourceCrawTabList.getTabList().get(1));
       taskResource=new ListResource();
       taskResource.load(uniqueList);
       task=new TaskVO<>(2,"爬取巨量云图单个搜索词",taskResource);
       crawlerSingleOceanEngineKeyword.crawSingleKeywords(uniqueList);
   }
}

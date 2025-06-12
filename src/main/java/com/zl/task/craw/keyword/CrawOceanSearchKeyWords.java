package com.zl.task.craw.keyword;

import com.zl.task.impl.taskResource.DefaultTaskResourceCrawTabList;

//获取巨量云图搜索词爬取测试用例

public class CrawOceanSearchKeyWords {
    public static void main(String[] args) throws Exception {
        crawler();

    }

    public static void crawler() throws Exception {
        CrawSeleniumOceanEngineKeyWords crawler = new CrawSeleniumOceanEngineKeyWords();
        String rName = "";
        int i = 1;
        while (!rName.equals("quit")) {
            crawler.setTab(DefaultTaskResourceCrawTabList.getTabList().get(i++));
            try {
                rName = CrawSeleniumOceanEngineKeyWords.crawAll(crawler, rName);
            } catch (Exception e) {
                e.printStackTrace();
                Thread.sleep(1000 * 60);
            }
        }

    }
}

package com.zl.task.executor.keyword;

import com.zl.task.craw.keyword.CrawSeleniumOceanEngineKeyWords;
import junit.framework.TestCase;
import org.junit.Test;

public class CrawSeleniumOceanEngineKeyWordsTest extends TestCase {

    @Test
    public void renameDownloadFile() throws Exception {
        CrawSeleniumOceanEngineKeyWords craw = new CrawSeleniumOceanEngineKeyWords();

        craw.renameDownloadFile("d:\\", "S:\\data\\task\\爬虫\\yunTu\\巨量云图行业搜索词\\", "2024-12-01", "食品饮料", 1735651800);
    }
}
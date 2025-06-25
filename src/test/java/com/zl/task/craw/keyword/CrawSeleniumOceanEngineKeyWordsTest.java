package com.zl.task.craw.keyword;

import com.zl.task.vo.task.taskResource.DefaultTaskResourceCrawTabList;
import com.zl.task.save.Saver;
import com.zl.task.vo.task.taskResource.TaskVO;
import org.junit.Before;
import org.junit.Test;

public class CrawSeleniumOceanEngineKeyWordsTest {
    CrawSeleniumOceanEngineKeyWords crawler;

    @Before
    public void setUp() throws Exception {
        crawler = new CrawSeleniumOceanEngineKeyWords();
        crawler.setTab(DefaultTaskResourceCrawTabList.getTabList().get(0));
        crawler.openEnterUrl("https://yuntu.oceanengine.com/yuntu_ng/search_strategy/search_words?aadvid=1760501554223111");

    }

    @Test
    public void run() throws Exception {
        TaskVO taskvo = new TaskVO(1, "云图搜索词");
        taskvo.setTaskDesc("服饰内衣/*/*");
        crawler.setFlag( true);
        crawler.run(taskvo);
    }

    @Test
    public void crawAll() throws Exception {
        //Saver.save();
        //云图搜索词
        crawler.setFlag(true); //设置巨量云图搜索词详情执行标志
        crawler.setSecondFlag(true);
        String rName = "";
        int i = 1;
        while (!rName.equals("quit")) {

            try {
                rName = CrawSeleniumOceanEngineKeyWords.crawAll(crawler, rName);
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    Thread.sleep(1000 * 60);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }

        }
    }
}
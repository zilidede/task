package com.zl.task.executor.craw.list;

import com.zl.task.craw.list.CrawSeleniumDouYinList;
import com.zl.task.impl.taskResource.DefaultTaskResourceCrawTabList;
import com.zl.task.vo.task.TaskVO;
import org.junit.Test;

public class CrawSeleniumDouYinListTest {

    @Test
    public void run() throws Exception {
        TaskVO taskvo = new TaskVO(0, "测试抖店罗盘小时榜单");
        taskvo.setTaskDesc("小时榜&内容&直播交易榜&服饰内衣-2");
        CrawSeleniumDouYinList crawler = new CrawSeleniumDouYinList();
        crawler.setTab(DefaultTaskResourceCrawTabList.getTabList().get(0));
        crawler.run(taskvo);
    }
}
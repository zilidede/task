package com.zl.task.impl.taskResource;

import com.ll.drissonPage.page.ChromiumPage;
import com.ll.drissonPage.page.ChromiumTab;
import com.zl.utils.webdriver.DefaultWebDriverUtils;

import java.util.ArrayList;
import java.util.List;

//默认的任务资源类
public class DefaultTaskResourceCrawTabList {
    private static List<ChromiumTab> tabList; //chromiumTab列表 默认为3个

    public static void getTaskResource() throws InterruptedException {
        generatorTabs(8);
    }

    public static void generatorTabs(int count) throws InterruptedException {
        //生产tab队列 =
        ChromiumPage chromiumPage = DefaultWebDriverUtils.getInstance().getDriver();
        //chromiumPage.setDownloadPath("D:\\data\\爬虫\\电商\\抖音\\yunTu\\巨量云图行业搜索词");
        tabList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Thread.sleep(1000 * 3);
            chromiumPage.newTab("http://www.baidu.com/");
            ChromiumTab tab = chromiumPage.getTab(1);
            tabList.add(tab);
        }

    }

    public static List<ChromiumTab> getTabList() {
        if (tabList == null) {
            try {
                getTaskResource();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return tabList;
    }
}

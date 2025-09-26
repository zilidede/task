package com.zl.task.vo.task.taskResource;

import com.ll.drissonPage.page.ChromiumPage;
import com.ll.drissonPage.page.ChromiumTab;
import com.zl.utils.webdriver.DefaultWebDriverUtils;

import java.util.ArrayList;
import java.util.List;

//默认的任务资源类
public class DefaultTaskResourceCrawTabList {
    private static List<ChromiumTab> tabList; //chromiumTab列表 默认为3个
    private static int tabCount = 1;  //chromiumTab列表数量;
    private static Integer  port=9223;

    public static void setPort(Integer port) {
        DefaultTaskResourceCrawTabList.port = port;
    }
    public static Integer getPort() {
        return port;
    }
    public static void setTabCount(int tabCount) {

    }

    public static int getTabCount() {
        return tabCount;
    }



    public static void getTaskResource() throws InterruptedException {
        generatorTabs(tabCount);
    }

    public static void generatorTabs(int count) throws InterruptedException {
        //生产tab队列 =
        DefaultWebDriverUtils.setPort(port);
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

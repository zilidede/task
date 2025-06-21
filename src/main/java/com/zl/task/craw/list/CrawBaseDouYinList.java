package com.zl.task.craw.list;

import com.ll.drissonPage.base.By;
import com.ll.drissonPage.element.ChromiumElement;
import com.ll.drissonPage.page.ChromiumTab;
import com.zl.task.craw.SaveXHR;
import com.zl.task.impl.ExecutorTaskService;
import com.zl.task.impl.taskResource.TaskResource;
import com.zl.task.vo.task.TaskVO;
import com.zl.utils.log.LoggerUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CrawBaseDouYinList implements ExecutorTaskService<String> {
    private final int maxCrawCount = 300; //最大爬取次数300
    private ChromiumTab tab; //当前爬取的tab页
    private int crawCount; //页面爬取次数；
    private String xhrSaveDir = ""; //xhr文件保存主目录
    private final List<String> xhrList = new ArrayList<>();
    private TaskVO task;

    public void init() {
        //资源初始化

    }

    public List<String> getXhrList() {
        return xhrList;
    }

    public void setXhrList(Map<String, String> map) {
        xhrList.clear();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            xhrList.add(entry.getValue());
        }
    }

    public String getXhrSaveDir() {
        return xhrSaveDir;
    }

    public void setXhrSaveDir(String xhrSaveDir) {
        this.xhrSaveDir = xhrSaveDir;
    }

    public ChromiumTab getTab() {
        return tab;
    }

    public void setTab(ChromiumTab tab) {
        this.tab = tab;
    }



    @Override
    public void ExecutorTaskService(TaskResource<String> taskResource) {

    }

    @Override
    public void ExecutorTaskService(Object object) {

    }

    @Override
    public void run(TaskVO task) throws Exception {
        //爬取循环
        this.task = task;
    }

    public void setListonXhr() throws InterruptedException {
        //设置监听xhr
        /* 举例说明
        List<String> list = new ArrayList<>();
        list.add("board_list");
        list.add("video_rank/hot_video_rank_v2_luopan");
        tab.listen().start(list); //监听商品榜单xhr
        Thread.sleep(4000);
         */
        tab.listen().start(xhrList);
    }

    public void save() {
        //导入到数据库
        SaveXHR.saveXhr(tab, xhrSaveDir, xhrList);
    }

    public void select(TaskVO task) throws InterruptedException {
        //确定爬取范围；
    }

    public void craw(TaskVO task) throws Exception {
        //爬取已经确认的选择内容信息；
    }

    public void openEnterUrl(String url) throws InterruptedException {
        tab.get(url);
        Thread.sleep(1000 * 10);
    }


    public boolean crawCompassList() throws Exception {
        //爬取已经选择的抖店罗盘榜单-翻页操作
        try {
            getTab().runJs("var q=document.documentElement; q.scrollTop = q.scrollHeight"); //滚动到顶部
            Thread.sleep(1000);
            pageTurnTwo(); //翻页操作
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        //
        //保存xhr文件
        save();
        return true;
    }

    public boolean pageTurnTwo() throws InterruptedException {
        //翻页操作-查看更多
        List<ChromiumElement> elements = null;
        String xpath = "//*[@class=\"ecom-pagination ecom-table-pagination ecom-table-pagination-right\"]/li";
        try {
            elements = getTab().eles(By.xpath(xpath)); //获取翻页列表
            Thread.sleep(1000);
        } catch (Exception ex) {
            ex.printStackTrace();
            LoggerUtils.logger.warn("获取失败，页面数为空");
            return false;
        }
        int len = -1;
        if (elements.size() == 0)
            return false;
        Integer len1 = 0;
        String s1 = elements.get(0).text().replace("共", "").replace("条", "");
        if (s1.equals("")) {
            s1 = elements.get(elements.size() - 2).attr("title");
            len = Integer.parseInt(s1);
        } else {

            try {
                len1 = Integer.parseInt(s1);
            } catch (Exception ex) {
                LoggerUtils.logger.warn("获取失败，页面数为空");
                return false;
            }
            len = len1 / 10 + (len1 % 10 == 0 ? 0 : 1);
            if (len < 0) {
                return false;
            }
        }
        for (int i = 0; i < len; i++) {
            try {
                xpath = "//*[@class=\"ecom-pagination-next\"]";
                ChromiumElement element = getTab().ele(By.xpath(xpath));
                element.click().click();
                crawCount++;
                Thread.sleep(2500);
                if (crawCount > maxCrawCount) {
                    LoggerUtils.logger.warn("翻页次数超过最大限制，休眠1小时");
                    Thread.sleep(60 * 60 * 1000);
                    crawCount = 0;
                }
            } catch (Exception ex) {
                System.out.println(i);
                ex.printStackTrace();
                continue;
            }

        }
        return true;
    }
}

package com.zl.task.craw.base;

import com.ll.drissonPage.page.ChromiumTab;
import com.zl.task.craw.SaveXHR;
import com.zl.task.impl.ExecutorTaskService;
import com.zl.task.impl.taskResource.TaskResource;
import com.zl.task.vo.task.TaskVO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CrawBaseXHR implements ExecutorTaskService<String> {
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
        //初始化任务资源
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



    public void craw(TaskVO task) throws Exception {
        //爬取已经确认的选择内容信息；
    }

    public void openEnterUrl(String url, Double timeout) throws InterruptedException {
        tab.get(url);
        Thread.sleep((long) (1000 * timeout));
    }
}

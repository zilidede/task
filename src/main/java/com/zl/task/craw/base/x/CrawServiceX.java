package com.zl.task.craw.base.x;


import com.ll.drissonPage.page.ChromiumTab;
import com.zl.task.vo.task.TaskVO;

//craw服务接口
public interface CrawServiceX {
    void run(TaskVO task) throws Exception;

    void openUrl(String url, Double timeout);

    ChromiumTab getTab();

    void setTab(ChromiumTab tab);
}

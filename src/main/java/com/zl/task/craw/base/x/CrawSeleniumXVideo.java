package com.zl.task.craw.base.x;

import com.ll.drissonPage.page.ChromiumTab;

public interface CrawSeleniumXVideo {
    void init(ChromiumTab tab); //初始化

    void craw();

    void save(); //保存为文件

    void download(); //下载

}

package com.zl.task.craw.account;

import com.ll.drissonPage.page.ChromiumTab;
import com.zl.task.craw.base.CrawServiceXHRTabImpl;

import java.io.IOException;

public class CrawAccountGoods extends CrawServiceXHRTabImpl {
    /**
     * 构造函数
     *
     * @param tab        浏览器标签页对象
     * @param xhr        xhr名称
     * @param xhrSaveDir xhr保存目录
     * @throws IOException 当初始化XHR监听失败时抛出
     */
    public CrawAccountGoods(ChromiumTab tab, String xhr, String xhrSaveDir) throws IOException {
        super(tab, xhr, xhrSaveDir);
    }
}

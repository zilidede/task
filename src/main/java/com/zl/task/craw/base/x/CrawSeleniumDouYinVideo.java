package com.zl.task.craw.base.x;

import com.ll.drissonPage.page.ChromiumTab;

import java.util.ArrayList;
import java.util.List;

//
// 日期：2024-11-28 爬取抖音视频
public class CrawSeleniumDouYinVideo implements CrawSeleniumXVideo {
    private ChromiumTab tab;

    @Override
    public void init(ChromiumTab tab) {
    }

    @Override
    public void craw() {

    }

    public List<String> getUrlList() {
        List<String> urls = new ArrayList<>();
        return urls;
    }

    @Override
    public void save() {

    }

    @Override
    public void download() {

    }
}

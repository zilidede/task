package com.zl.task.vo.task.taskResource;

import com.ll.drissonPage.page.ChromiumTab;

import java.util.List;

//
public class TaskResourceCrawImpl implements TaskResource<ChromiumTab> {
    private ChromiumTab tab;


    @Override
    public ChromiumTab initResource(Object... params) {
        return null;
    }

    @Override
    public void processResource(ChromiumTab resource) {

    }

    @Override
    public void load(ChromiumTab chromiumTab) {
        tab = (ChromiumTab) chromiumTab;
    }

    @Override
    public ChromiumTab getT() {
        return null;
    }
}

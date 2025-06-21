package com.zl.task.impl.taskResource;

import com.ll.drissonPage.page.ChromiumTab;

import java.util.Collection;

//
public class TaskResourceCrawImpl implements TaskResource<ChromiumTab> {
    private ChromiumTab tab;



    @Override
    public void load(ChromiumTab chromiumTab) {
        tab = (ChromiumTab) chromiumTab;
    }
}

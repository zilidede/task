package com.zl.task.impl.taskResource;

import com.ll.drissonPage.page.ChromiumTab;

//
public class TaskResourceCrawImpl implements TaskResource {
    private ChromiumTab tab;


    @Override
    public void load(Object obj) {
        tab = (ChromiumTab) obj;
    }
}

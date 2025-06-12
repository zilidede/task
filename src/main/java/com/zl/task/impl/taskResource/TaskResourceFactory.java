package com.zl.task.impl.taskResource;

import com.ll.drissonPage.page.ChromiumTab;

import java.util.List;

//任务资源工厂类
public class TaskResourceFactory {
    private static int i = 0;

    public static TaskResource createTaskResource(String type) {
        if ("craw".equalsIgnoreCase(type)) {
            Object taskRes;
            List<ChromiumTab> tabList = DefaultTaskResourceCrawTabList.getTabList();
            TaskResource res = null;
            if (i < tabList.size())
                res = (TaskResource) tabList.get(i++);
            else {
                res = (TaskResource) tabList.get(i++);
                i = 0;
            }
            return res;
        } else if ("docx".equalsIgnoreCase(type)) {
            // return new DocxDocument();
        } else if ("save".equalsIgnoreCase(type)) {
            return null;
        } else {
            //throw new IllegalArgumentException("Unsupported document type: " + type);
        }
        return null;
    }
}

package com.zl.task.vo.task.taskResource;

import com.ll.drissonPage.page.ChromiumTab;

import java.util.List;


//
public class TaskResourceListImpl  implements TaskResource<List<String>> {

    private List<String> strings;
    @Override
    public void load(List<String> strings) {
        this.strings = strings;
    }

    @Override
    public List<String> getT() {
        return strings;
    }

    @Override
    public List<String> initResource(Object... params) {
        return null;
    }


    @Override
    public void processResource(List<String> resource) {

    }

    @Override
    public void load(ChromiumTab chromiumTab) {

    }
}

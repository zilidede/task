package com.zl.task.vo.task.taskResource;

import com.ll.drissonPage.page.ChromiumTab;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListResource implements TaskResource<List<String>> {
    private List<String> list;
    @Override
    public void load(List<String> strings) {
        this.list=strings;
    }

    @Override
    public List<String> getT() {
        return list;
    }

    @Override
    public List<String> initResource(Object... params) {
        List<String> list = new ArrayList<>();

        if (params.length > 0 && params[0] instanceof Integer) {
            int count = (Integer) params[0];
            for (int i = 1; i <= count; i++) {
                list.add("Item " + i);
            }
        } else {
            list.add("Apple");
            list.add("Banana");
            list.add("Cherry");
        }

        return list;
    }

    @Override
    public void processResource(List<String> resource) {
        System.out.println("List Contents (" + resource.size() + " items):");
        resource.forEach(item -> System.out.println("- " + item));
    }

    @Override
    public void load(ChromiumTab chromiumTab) {

    }
}
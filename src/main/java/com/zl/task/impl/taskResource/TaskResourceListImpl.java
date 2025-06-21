package com.zl.task.impl.taskResource;

import java.util.List;


//
public class TaskResourceListImpl  implements TaskResource<List<String>> {

    private List<String> strings;
    @Override
    public void load(List<String> strings) {
        this.strings = strings;
    }
}

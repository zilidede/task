package com.zl.task.vo.other;

import java.util.*;

public class GenericListContainerVO {
    private Map<Class<?>, List<?>> lists = new HashMap<>();

    public <T> void addList(Class<T> type, List<T> list) {
        lists.put(type, list);
    }

    public <T> List<T> getList(Class<T> type) {
        return (List<T>) lists.get(type);
    }
}

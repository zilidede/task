package com.zl.task.impl.taskResource;

import java.util.Collection;
import java.util.List;

// 任务资源工厂类
public interface TaskResource<T > {
    void load(T t);


}

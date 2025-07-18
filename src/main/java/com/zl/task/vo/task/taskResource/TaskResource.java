package com.zl.task.vo.task.taskResource;

import com.ll.drissonPage.page.ChromiumTab;

import java.util.Collection;
import java.util.List;

// 任务资源工厂类
public interface TaskResource<T > {

    public void load(T strings);

    public T getT();

    /**
     * 初始化资源（带参数）
     * @param params 初始化参数，类型为 Object[]
     * @return 初始化的资源对象
     */
    T initResource(Object... params);

    /**
     * 处理资源
     */
    void processResource(T resource);

    /**
     * 释放资源
     */
    default void releaseResource(T resource) {
        // 默认空实现，子类可按需重写
    }


    void load(ChromiumTab chromiumTab);
}

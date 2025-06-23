package com.zl.task.impl;


import com.zl.task.vo.task.taskResource.TaskResource;
import com.zl.task.vo.task.taskResource.TaskVO;

//任务执行器接口
public interface ExecutorTaskService<T> {
    void ExecutorTaskService(TaskResource<T> taskResource);

    void ExecutorTaskService(Object object);

    void run(TaskVO task) throws Exception;


}

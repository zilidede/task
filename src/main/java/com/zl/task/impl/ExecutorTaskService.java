package com.zl.task.impl;


import com.zl.task.impl.taskResource.TaskResource;
import com.zl.task.vo.task.TaskVO;

//任务执行器接口
public interface ExecutorTaskService {
    void ExecutorTaskService(TaskResource taskResource);

    void run(TaskVO task) throws Exception;


}

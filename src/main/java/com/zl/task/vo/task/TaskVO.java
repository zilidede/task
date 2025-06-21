// Task.java
package com.zl.task.vo.task;


import com.zl.task.impl.ExecutorTaskService;
import com.zl.task.impl.taskResource.TaskResource;

import java.util.Collection;
import java.util.Date;

public class TaskVO<T> {
    private int taskId; //任务id号
    private String taskUuid = "0l"; //任务唯一标识
    private String taskName; //任务名
    private String taskDesc; //任务描述
    private Date executeTime; //任务运行时间
    private TaskResource<T> taskResource; //任务资源

    private int status;

    public TaskResource getTaskResource() {
        return taskResource;
    }

    public void setTaskResource(TaskResource<T> taskResource) {
        this.taskResource = taskResource;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    private Date finishTime; //任务完成时间

    public Date getFinishTime() {
        return finishTime;
    }

    public Date getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(Date executeTime) {
        this.executeTime = executeTime;
    }

    public void setFinishTime(Date finishTime) {
    }

    private String taskType;

    public String getTaskDesc() {
        return taskDesc;
    }

    public void setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc;
    }

    private ExecutorTaskService executor;

    public TaskVO(int taskId, String taskName) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.taskUuid = generateUniqueTaskUuid(); // 假设这个方法会生成一个唯一的UUID
    }

    // 其他getter方法省略...

    private String generateUniqueTaskUuid() {
        // 生成并返回一个唯一的UUID
        return "0l";
    }

    public String getTaskUuid() {
        return taskUuid;
    }

    public void setTaskUuid(String taskUuid) {
        this.taskUuid = taskUuid;
    }

    public ExecutorTaskService getExecutor() {
        return executor;
    }

    public void setExecutor(ExecutorTaskService executor) {
        this.executor = executor;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getTaskId() {
        return taskId;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }


    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskName() {
        return taskName;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskuuId=" + taskUuid +
                ", taskName='" + taskName + '\'' +
                '}';
    }
}
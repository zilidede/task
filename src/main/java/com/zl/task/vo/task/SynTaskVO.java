package com.zl.task.vo.task;

import java.util.Date;

/**
 * @className: com.craw.nd.vo.task-> SynTask
 * @description:
 * @author: zl
 * @createDate: 2024-02-02 12:06
 * @version: 1.0
 * @todo:
 */
public class SynTaskVO {
    private String taskName; //任务名称
    private String host; //主机ip
    private Date startDate; //开始时间
    private Date endDate; //结束时间
    private String srcDir;//源目录
    private String desDir;//目标目录

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public String getDesDir() {
        return desDir;
    }

    public String getSrcDir() {
        return srcDir;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setDesDir(String desDir) {
        this.desDir = desDir;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setSrcDir(String srcDir) {
        this.srcDir = srcDir;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
}

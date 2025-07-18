package com.zl.config;

/**
 * @className: com.craw.nd.config-> Config
 * @description:
 * @author: zl
 * @createDate: 2023-02-05 12:19
 * @version: 1.0
 * @todo:
 */
public class Config {
    public static String port = "127.0.0.1:9223";
    public static Integer FAIL_INSERT = -111; // sql插入失败；
    public static Integer FAIL_UPDATE = -111; // sql更新失败；
    public static Integer INFO_HTTP_TASK_CLOSE = -1001;//HTTP任务结束
    public static Integer WARM_HTTP_TASK_EX = -1002;//HTTP任务异常-无法获取内容
    public static Integer INFO_SINGLE_TASK_END_TAG = -200; //单次爬取任务结束标识；
    public static final int CRAW_TASK_OK = -2; // 爬取任务已完成；
    public static final int CRAW_TASK_PAUSE = 2;//爬取任务暂停


}


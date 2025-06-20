package com.zl.utils.other;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @className: com.craw.nd.util-> LoggerUtils
 * @description: Log4j2工具类
 * @author: zl
 * @createDate: 2022-12-24 14:18
 * @version: 1.0
 * @todo:
 */
public class LogBackUtils {
    private static final Logger logger = LoggerFactory.getLogger(LogBackUtils.class);

    public static void main(String[] args) throws Exception {
        // logback 日志的5种级别的输出方式
        // 从低到高依次是 trace < debug < info < warn < error
        Logger logger = LoggerFactory.getLogger(LogBackUtils.class);
        logger.error("error 错误信息，不会影响系统运行");
        logger.warn("warn 警告信息,可能会发生问题");
        logger.info("info 运行信息，数据连接、网络连接、I0操作等等");
        logger.debug("debug 调试信息，一般在开发中使用，记录程序变量参数传递信息等等");
        logger.trace("trace 追踪信息，记录程序所有的流程信息");
    }


}

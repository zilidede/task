package com.zl.utils.log;

import static org.junit.Assert.*;

public class LoggerUtilsTest {
    @org.junit.Test
    public void setLoggerLevel() throws Exception {
        // 输出 DEBUG 日志
        LoggerUtils.logger.debug("This is a DEBUG log message.");

        // 输出其他级别的日志（例如 INFO、WARN、ERROR）
        LoggerUtils.logger.info("This is an INFO log message.");
        LoggerUtils.logger.warn("This is a WARN log message.");
        LoggerUtils.logger.error("This is an ERROR log message.");
    }

}
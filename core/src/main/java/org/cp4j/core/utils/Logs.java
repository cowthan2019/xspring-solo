package org.cp4j.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Logs {

    private static final Logger logger = LoggerFactory.getLogger(Logs.class);


    public static void info(String template, Object... args) {
        logger.info(template, args);
    }

    public static void warn(String format, Object... arguments) {
        logger.warn(format, arguments);
    }

    public static void error(String format, Object... arguments) {
        logger.error(format, arguments);
    }

    public static void error(String msg, Throwable t) {
        logger.error(msg, t);
    }


    public static void main(String[] args) {
        Logs.info("这是一个{}日志--{}", "info", null);
        Logs.warn("warn日志");
        Logs.error("error日志");
        Logs.error("处理出错", new RuntimeException("各种异常"));
    }
}

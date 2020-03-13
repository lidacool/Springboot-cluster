package com.lee.util.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Logging {

    private static Logger logger = getLogger(LoggerFactory.class);

    private static Logger getLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }

    private static Logger getLoggerByCaller() {
        return getLogger(Reflection.getCallerClass(2));
    }

    public static boolean isErrorEnabled() {
        return logger.isErrorEnabled();
    }

    public static void error(String msg) {
        getLoggerByCaller().error(msg);
    }

    public static void error(String format, Object... arguments) {
        getLoggerByCaller().error(format, arguments);
    }

    public static boolean isTraceEnabled() {
        return logger.isTraceEnabled();
    }

    public static void trace(String msg) {
        getLoggerByCaller().trace(msg);
    }

    public static boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    public static void debug(String msg) {
        getLoggerByCaller().debug(msg);
    }

    public static boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    public static void info(String msg) {
        getLoggerByCaller().info(msg);
    }

    public static void info(String msg,String...args) {
        getLoggerByCaller().info(msg,args);
    }

    public static void info(String format, Object... arguments) {
        getLoggerByCaller().info(format, arguments);
    }


    public static boolean isWarnEnabled() {
        return logger.isWarnEnabled();
    }

    public static void warn(String msg) {
        getLoggerByCaller().warn(msg);
    }

}

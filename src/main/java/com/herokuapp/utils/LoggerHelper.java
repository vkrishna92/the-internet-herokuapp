package com.herokuapp.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggerHelper {

    // Get logger for any class
    public static Logger getLogger(Class<?> clazz) {
        return LogManager.getLogger();
    }
}

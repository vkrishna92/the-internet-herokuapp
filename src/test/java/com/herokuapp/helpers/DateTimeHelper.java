package com.herokuapp.helpers;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public final class DateTimeHelper {

    private static DateTimeHelper instance;
    private DateTimeHelper() {

    }

    public static DateTimeHelper getInstance() {
        if (instance == null) {
            instance = new DateTimeHelper();
        }
        return instance;
    }

    public String getTimeStamp() {
        return DateTimeFormatter
                .ofPattern("yyyyMMdd_HHmmss_SSS")
                .withZone(ZoneId.systemDefault())
                .format(Instant.now());
    }
}

package com.mhandharbeni.e_parking.utils;

import android.text.format.DateFormat;

import java.util.Date;

public class UtilDate {
    public static String longToDate(String timestamp, String format) {
        long millisecond = Long.parseLong(timestamp);
        // or you already have long value of date, use this instead of milliseconds variable.
        return DateFormat.format(format, new Date(millisecond)).toString();
    }

    public static String longToDate(long timestamp, String format) {
        // or you already have long value of date, use this instead of milliseconds variable.
        return DateFormat.format(format, new Date(timestamp)).toString();
    }

}

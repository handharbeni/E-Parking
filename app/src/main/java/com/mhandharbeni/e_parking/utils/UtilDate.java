package com.mhandharbeni.e_parking.utils;

import android.annotation.SuppressLint;
import android.text.format.DateFormat;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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

    public static long getNow() {
        try {
            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String formattedDate = df.format(c);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date date = sdf.parse(formattedDate);
            long startDate = date.getTime();
            return startDate;
        } catch (ParseException ignored) {
            return 0;
        }
    }

}

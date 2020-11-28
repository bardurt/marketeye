package com.zygne.stockanalyzer.domain.utils;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeHelper {

    private static final int MS_IN_SECONDS = 1000;

    private static final int SEC_IN_DAY = 86400;

    private static final String dateFormat = "yyyy-MM-dd";

    private static final String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";

    public static int getDaysDifference(long start, long end) {

        long diff = start - end;

        long days = diff / (MS_IN_SECONDS * SEC_IN_DAY);

        return (int) days;
    }

    public static long getTimeStamp(String dateString) {

        DateFormat df;
        if(dateString.length() > 11) {
            df = new SimpleDateFormat(dateTimeFormat);
        } else {
            df = new SimpleDateFormat(dateFormat);
        }

        Date date;
        try {
            date = df.parse(dateString);
        } catch (ParseException e) {
            return 0;
        }

        return date.getTime();
    }

    public static String getDateFromTimeStamp(long timeStamp){
        // 2020-10-19 18:30:00

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timeStamp);

        Format format = new SimpleDateFormat(dateTimeFormat);
        return format.format(c.getTime());
    }

}

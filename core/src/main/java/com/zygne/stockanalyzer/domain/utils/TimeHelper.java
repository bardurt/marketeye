package com.zygne.stockanalyzer.domain.utils;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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

    public static String getDateTimeFromTimeStamp(long timeStamp){
        // 2020-10-19 18:30:00

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timeStamp);

        Format format = new SimpleDateFormat(dateTimeFormat);
        return format.format(c.getTime());
    }

    public static String getDateFromTimeStamp(long timeStamp){
        // 2020-10-19 18:30:00

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timeStamp);

        Format format = new SimpleDateFormat(dateFormat);
        return format.format(c.getTime());
    }

    public static boolean isSameDay(long timeStamp1, long timeStamp2){

        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTimeInMillis(timeStamp1);
        cal2.setTimeInMillis(timeStamp2);

        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)
                && cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
    }
}

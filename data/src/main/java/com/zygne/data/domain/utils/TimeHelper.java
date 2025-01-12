package com.zygne.data.domain.utils;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimeHelper {

    private static final String dateFormat = "yyyy-MM-dd";


    public static String getDateFromTimeStamp(long timeStamp){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timeStamp);

        Format format = new SimpleDateFormat(dateFormat);
        return format.format(c.getTime());
    }

    public static int getYearFromTimeStamp(long timeStamp){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timeStamp);
        return c.get(Calendar.YEAR);
    }

}

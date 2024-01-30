package com.zygne.chart.chart.model.data;

import java.util.Calendar;

public class TimeBox {

    private int year;
    private int month;
    private int day;

    public TimeBox(long timeStamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }


    public boolean isSameMonth(long timeStamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);

        if (this.year == calendar.get(Calendar.YEAR)) {
            if (this.month == calendar.get(Calendar.MONTH)+1) {
                return true;
            }
        }


        return false;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }
}

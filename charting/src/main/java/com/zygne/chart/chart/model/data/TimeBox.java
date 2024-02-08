package com.zygne.chart.chart.model.data;

import java.util.Calendar;

public class TimeBox {

    private final int year;
    private final int month;
    private final int day;

    public TimeBox(long timeStamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }


    public boolean isSameMonth(long timeStamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);

        if (this.year == calendar.get(Calendar.YEAR)) {
            return this.month == calendar.get(Calendar.MONTH) + 1;
        }

        return false;
    }

    public int getYear() {
        return year;
    }

    public String getYearString() {
        return "" + year;
    }

    public int getMonth() {
        return month;
    }

    public String getMonthString(){
        String value = "";

        if(month < 10){
            value = "0"+month;
        } else {
            value =""+month;
        }

        return value;
    }

    public int getDay() {
        return day;
    }

    public String getDayString(){
        String value = "";

        if(day < 10){
            value = "0"+day;
        } else {
            value =""+day;
        }

        return value;
    }
}

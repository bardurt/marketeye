package com.zygne.chart.chart.model.data;

import java.util.Calendar;

public abstract class Serie {

    protected int index = 0;
    protected String name = "";
    protected long timeStamp = 0l;
    protected DateFormat dateFormat;

    public enum DateFormat{
        MONTH,
        FULL
    }


    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public DateFormat getDateFormat(){
        return dateFormat;
    }

    public void setDateFormat(DateFormat dateFormat){
        this.dateFormat = dateFormat;
    }

    public void setDate(int day, int month, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.MONTH, month);

        timeStamp = calendar.getTimeInMillis();

    }
}


package com.zygne.stockanalyzer.domain.model;

import com.zygne.stockanalyzer.domain.utils.NumberHelper;
import com.zygne.stockanalyzer.domain.utils.TimeHelper;

public class BarData {

    private static final String DELIM = ",";

    private String time;
    private double open;
    private double high;
    private double low;
    private double close;
    private long volume;

    public BarData(String time, double open, double high, double low, double close, long volume) {
        this.time = time;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    @Override
    public boolean equals(Object obj) {
        return getTime().equalsIgnoreCase(((BarData)obj).getTime());
    }

    public static String toStream(BarData barData) {
        return barData.getTime() + DELIM + barData.open + DELIM + barData.getHigh() + DELIM + barData.getLow() + DELIM + barData.getClose() + DELIM + barData.getVolume();
    }

    public static BarData fromStream(String stream) {

        String[] tempArr;
        BarData barData = null;

        try {
            tempArr = stream.split(DELIM);
            String timeStamp = tempArr[0];
            double open = Double.parseDouble(tempArr[1]);
            double high = Double.parseDouble(tempArr[2]);
            double low = Double.parseDouble(tempArr[3]);
            double close = Double.parseDouble(tempArr[4]);
            long volume = Long.parseLong(tempArr[5]);

            barData = new BarData(timeStamp, open, high, low, close, volume);
        } catch (Exception e){
            e.printStackTrace();
        }

        return barData;
    }

}

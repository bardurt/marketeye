package com.zygne.stockanalyzer.domain.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class BarData {

    public static final class TimeStampFormat {
        public static final String ALPHA_VANTAGE = "yyyy-MM-dd HH:mm:ss";
        public static final String ALPHA_VANTAGE_SIMPLE = "yyyy-MM-dd";
        public static final String INTERACTIVE_BROKERS = "yyyyMMdd HH:mm:ss";
        public static final String INTERACTIVE_BROKERS_SIMPLE = "yyyyMMdd";
        public static final String YAHOO_FINANCE = "yyyy-MM-dd";
        public static final String YAHOO_FINANCE_SIMPLE = "yyyy-MM-dd";
    }

    public static final class DataFarm{
        public static final int UNKNOWN = -1;
        public static final int INTERACTIVE_BROKERS = 0;
        public static final int ALPHA_VANTAGE = 1;
        public static final int YAHOO = 2;
    }

    public static final class Asset{
        public static final int STOCK = 0;
        public static final int CRYPTO = 1;
    }

    private static final String DELIM = ",";

    private String time;
    private double open;
    private double high;
    private double low;
    private double close;
    private long volume;
    private int dataFarm = DataFarm.UNKNOWN;
    private int asset = Asset.STOCK;

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

    public int getDataFarm() {
        return dataFarm;
    }

    public void setDataFarm(int dataFarm) {
        this.dataFarm = dataFarm;
    }

    public static String getDELIM() {
        return DELIM;
    }

    public long getTimeStamp(){

        String dateFormat = "";
        if(dataFarm == DataFarm.ALPHA_VANTAGE){

            if(time.length() > 11){
                dateFormat = TimeStampFormat.ALPHA_VANTAGE;
            } else {
                dateFormat = TimeStampFormat.ALPHA_VANTAGE_SIMPLE;
            }
        } else if(dataFarm == DataFarm.INTERACTIVE_BROKERS){
            if(time.length() > 8){
                dateFormat = TimeStampFormat.INTERACTIVE_BROKERS;
            } else {
                dateFormat = TimeStampFormat.INTERACTIVE_BROKERS_SIMPLE;
            }

        } else if(dataFarm == DataFarm.YAHOO){
            if(time.length() > 8){
                dateFormat = TimeStampFormat.YAHOO_FINANCE;
            } else {
                dateFormat = TimeStampFormat.YAHOO_FINANCE_SIMPLE;
            }

        }

        DateFormat df = new SimpleDateFormat(dateFormat);

        Date date;

        try {
            date = df.parse(time);
        } catch (ParseException e) {
            return 0;
        }

        return date.getTime();
    }

    public static String toStream(BarData barData) {
        return barData.getTime() + DELIM + barData.open + DELIM + barData.getHigh() + DELIM + barData.getLow() + DELIM + barData.getClose() + DELIM + barData.getVolume() + DELIM + barData.getDataFarm();
    }

    public static String createHeaders(){
        return "Time" + DELIM + "Open" + DELIM + "High" + DELIM + "Low" + DELIM + "Close" + DELIM + "Volume" + DELIM + "Origin";
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

            int farm = DataFarm.UNKNOWN;

            try{
                farm = Integer.parseInt(tempArr[6]);
            } catch (Exception ignored){ }

            barData = new BarData(timeStamp, open, high, low, close, volume);
            barData.setDataFarm(farm);
        } catch (Exception e){
            e.printStackTrace();
        }

        return barData;
    }

    public static BarData fromStream(String stream, int dataFarm) {

        String[] tempArr;
        BarData barData = null;

        if(dataFarm == DataFarm.YAHOO){

            try {
                tempArr = stream.split(DELIM);
                String timeStamp = tempArr[0];
                double open = Double.parseDouble(tempArr[1]);
                double high = Double.parseDouble(tempArr[2]);
                double low = Double.parseDouble(tempArr[3]);
                double close = Double.parseDouble(tempArr[4]);
                long volume = Long.parseLong(tempArr[6]);

                barData = new BarData(timeStamp, open, high, low, close, volume);
                barData.setDataFarm(dataFarm);
            } catch (Exception ignored){ }
        }

        return barData;
    }

    public static BarData fromStream(String stream, int dataFarm, int asset) {

        BarData barData = null;
        if(dataFarm == DataFarm.ALPHA_VANTAGE && asset == Asset.CRYPTO){

            String[] tempArr;

            try {
                tempArr = stream.split(DELIM);
                String timeStamp = tempArr[0];
                double open = Double.parseDouble(tempArr[1]);
                double high = Double.parseDouble(tempArr[2]);
                double low = Double.parseDouble(tempArr[3]);
                double close = Double.parseDouble(tempArr[4]);
                double volume = Double.parseDouble(tempArr[9]);

                int farm = DataFarm.UNKNOWN;

                try{
                    farm = Integer.parseInt(tempArr[6]);
                } catch (Exception ignored){ }

                barData = new BarData(timeStamp, open, high, low, close, (long) volume);
                barData.setDataFarm(farm);
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        return barData;
    }


    public static final class TimeComparator implements Comparator<BarData> {

        @Override
        public int compare(BarData o1, BarData o2) {
            return Long.compare(o1.getTimeStamp(), o2.getTimeStamp());
        }
    }

    @Override
    public boolean equals(Object obj) {
        return getTime().equalsIgnoreCase(((BarData)obj).getTime());
    }

}

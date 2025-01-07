package com.zygne.data.domain.model;

import com.zygne.data.domain.FinanceData;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public record BarData(String time,
                      double open,
                      double high,
                      double low,
                      double close,
                      long volume) implements FinanceData {

    public static final class TimeStampFormat {
        public static final String YAHOO_FINANCE = "yyyy-MM-dd";
        public static final String YAHOO_FINANCE_SIMPLE = "yyyy-MM-dd";
    }

    private static final String DELIM = ",";


    @Override
    public long getTimeStamp() {

        String dateFormat;

        if (time.length() > 8) {
            dateFormat = TimeStampFormat.YAHOO_FINANCE;
        } else {
            dateFormat = TimeStampFormat.YAHOO_FINANCE_SIMPLE;
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

    public static BarData fromStreamYahoo(String stream) {

        String[] tempArr;
        BarData barData = null;

        try {
            tempArr = stream.split(DELIM);
            String timeStamp = tempArr[0];
            double open = Double.parseDouble(tempArr[1]);
            double high = Double.parseDouble(tempArr[2]);
            double low = Double.parseDouble(tempArr[3]);
            double close = Double.parseDouble(tempArr[4]);
            long volume = Long.parseLong(tempArr[6]);

            barData = new BarData(timeStamp, open, high, low, close, volume);
        } catch (Exception ignored) {
        }
        return barData;
    }

    public static BarData fromStreamAlphaVantage(String stream) {

        String[] tempArr;
        BarData barData = null;

        try {
            tempArr = stream.split(DELIM);
            String timeStamp = tempArr[0];
            double open = Double.parseDouble(tempArr[1]);
            double high = Double.parseDouble(tempArr[2]);
            double low = Double.parseDouble(tempArr[3]);
            double close = Double.parseDouble(tempArr[4]);
            long volume = (long) (Double.parseDouble(tempArr[5]));

            barData = new BarData(timeStamp, open, high, low, close, volume);
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
        return barData;
    }

    public static BarData fromStreamBitstamp(String stream) {

        String[] tempArr;
        BarData barData = null;

        try {
            tempArr = stream.split(DELIM);
            String timeStamp = tempArr[1];
            double open = Double.parseDouble(tempArr[3]);
            double high = Double.parseDouble(tempArr[4]);
            double low = Double.parseDouble(tempArr[5]);
            double close = Double.parseDouble(tempArr[6]);
            long volume = (long) (Double.parseDouble(tempArr[8]));

            barData = new BarData(timeStamp, open, high, low, close, volume);
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
        return barData;
    }

    @Override
    public boolean equals(Object obj) {
        return time().equalsIgnoreCase(((BarData) obj).time());
    }

}

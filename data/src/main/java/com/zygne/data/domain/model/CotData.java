package com.zygne.data.domain.model;

import com.zygne.data.domain.FinanceData;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public record CotData(
        String name,
        String time,
        double longPositions,
        double shortPositions) implements FinanceData {

    public double getNet() {
        return longPositions - shortPositions;
    }

    public static CotData fromStream(String stream) {

        String[] parts = stream.split("\\|");

        String time = parts[0];
        String name = parts[1];
        double longs = Double.parseDouble(parts[2]);
        double shorts = Double.parseDouble(parts[3]);

        return new CotData(name, time, longs, shorts);
    }

    @Override
    public long getTimeStamp() {
        DateFormat df = new SimpleDateFormat("yyMMdd");

        Date date;

        try {
            date = df.parse(time);
        } catch (ParseException e) {
            return 0;
        }

        return date.getTime();
    }

    @Override
    public String toString() {
        return "Time " + time + ", name " + name + ", longs " + longPositions + ", shorts " + shortPositions + ", net " + getNet();
    }
}

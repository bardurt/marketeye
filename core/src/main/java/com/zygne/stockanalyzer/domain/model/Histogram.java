package com.zygne.stockanalyzer.domain.model;

import java.util.Comparator;

public class Histogram {
    public long timeStamp;
    public double open;
    public double high;
    public double low;
    public double close;
    public long volume;

    public Direction getDirection() {
        if (open < close) {
            return Direction.Up;
        } else {
            return Direction.Down;
        }
    }

    public double getTotalRange(){
        return ((high - low) / low)*100;
    }

    public double getBodyRange(){
        return ((close - open) / open)*100;
    }

    public double getOpenHighRange(){
        return ((high - open) / open)*100;
    }

    public enum Direction {Up, Down}

    public boolean intersects(double value){
        if(value <= high){
            return value >= low;
        }

        return false;
    }

    public boolean inBody(double value){
        if(getDirection() == Direction.Up){
            if(value <= close){
                return value >= open;
            }
        } else {
            if(value >= close){
                return value <= open;
            }
        }

        return false;
    }

    @Override
    public boolean equals(Object obj) {
        long timeStamp1 = ((Histogram)obj).timeStamp;
        return timeStamp == timeStamp1;
    }

    public static final class TimeComparator implements Comparator<Histogram> {

        @Override
        public int compare(Histogram o1, Histogram o2) {
            return Long.compare(o1.timeStamp, o2.timeStamp);
        }
    }

}

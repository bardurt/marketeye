package com.zygne.stockalyze.domain.model;

import com.zygne.stockalyze.domain.model.enums.TimeFrame;

import java.util.Comparator;

public class Histogram {
    public int timeStamp;
    public int open;
    public int high;
    public int low;
    public int close;
    public long volume;
    public TimeFrame timeFrame;
    public double decay = 1;

    public Direction getDirection() {
        if (open < close) {
            return Direction.Up;
        } else {
            return Direction.Down;
        }
    }

    public double getTotalRange(){
        return ((high - low) / (double)low)*100;
    }

    public double getBodyRange(){
        return ((close - open) / (double)open)*100;
    }

    public double getOpenHighRange(){
        return ((high - open) / (double)open)*100;
    }

    public enum Direction {Up, Down}


    public static class TimeComparator implements Comparator<Histogram>{

        @Override
        public int compare(Histogram o1, Histogram o2) {
            return Integer.compare(o1.timeStamp, o2.timeStamp);
        }
    }
}

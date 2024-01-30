package com.zygne.chart.chart.util;

import java.util.LinkedList;
import java.util.Queue;

public class SimpleMovingAverage {

    private final Queue<Long> dataSet = new LinkedList<Long>();
    private final int period;
    private long sum;

    // constructor to initialize period
    public SimpleMovingAverage(int period) {
        this.period = period;
    }

    // function to add new data in the
    // list and update the sum so that
    // we get the new mean
    public void addData(Long num) {
        sum += num;
        dataSet.add(num);

        // Updating size so that length
        // of data set should be equal
        // to period as a normal mean has
        if (dataSet.size() > period) {
            sum -= dataSet.remove();
        }
    }

    // function to calculate mean
    public long getMean() {
        return sum / period;
    }

}

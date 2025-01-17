package com.zygne.data.domain.model;

import java.util.LinkedList;
import java.util.Queue;

public class SimpleMovingAverage {

    private final Queue<Double> dataSet = new LinkedList<>();
    private final int period;
    private long sum;

    public SimpleMovingAverage(int period) {
        this.period = period;
    }

    public void addData(double num) {
        sum += num;
        dataSet.add(num);

        if (dataSet.size() > period) {
            sum -= dataSet.remove();
        }
    }

    public double getMean() {
        return sum / (double)period;
    }
}

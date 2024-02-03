package com.zygne.data.domain.utils;

import java.util.LinkedList;
import java.util.Queue;

public class SimpleMovingAverage {

    private final Queue<Double> dataSet = new LinkedList<Double>();
    private final int period;
    private double sum;

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
        return sum / period;
    }

    public static void main(String[] args) {
        double[] input_data = {1, 3, 5, 6, 8,
                12, 18, 21, 22, 25};
        int per = 3;
        SimpleMovingAverage obj = new SimpleMovingAverage(per);
        for (double x : input_data) {
            obj.addData(x);
            System.out.println("New number added is " +
                    x + ", SMA = " + obj.getMean());
        }
    }
}
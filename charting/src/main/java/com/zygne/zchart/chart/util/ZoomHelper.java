package com.zygne.zchart.chart.util;

public class ZoomHelper {

    private static final double[] scales = {
            0.001d,
            0.005d,
            0.01d,
            0.025d,
            0.05,
            0.1,
            0.25,
            0.5,
            1,
            2,
            5,
            10,
            20,
            50,
            100,
            250,
            500,
            1000,
            5000};


    public static double getScalar(int index) {

        if (index < 0) {
            return scales[0];
        }

        if (index > scales.length) {
            return scales[scales.length - 1];
        }

        double scale = scales[index];

        System.out.println("Scale " + scale);

        return scales[index];
    }

    public static int getMax() {
        return scales.length;
    }

}

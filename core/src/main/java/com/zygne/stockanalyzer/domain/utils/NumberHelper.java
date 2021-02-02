package com.zygne.stockanalyzer.domain.utils;

public class NumberHelper {

    public static double round2Decimals(double original){
        return Math.round(original * 100d) / 100d;
    }

    public static double roundDecimals(int decimals, double original){
        double scalar = Math.pow(10, decimals);

        return Math.round(original * scalar) / scalar;
    }
}

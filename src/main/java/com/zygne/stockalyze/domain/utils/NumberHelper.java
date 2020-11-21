package com.zygne.stockalyze.domain.utils;

public class NumberHelper {

    public static double round2Decimals(double original){
        return Math.round(original * 100d) / 100d;
    }

}

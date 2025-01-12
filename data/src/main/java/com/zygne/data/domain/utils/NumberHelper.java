package com.zygne.data.domain.utils;


import static java.lang.Math.round;

public class NumberHelper {

    public static double round2Decimals(double original) {
        return round(original * 100d) / 100d;
    }
}

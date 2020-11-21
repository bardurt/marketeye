package com.zygne.stockalyze.utils;


public class ColorHelper {

    public static String getColorForPercentile(double percentile){

        String color = null;

        if (percentile > 98) {
            color = "#FF7100";
        }

        if(percentile > 99){
            color = "FF0000";
        }

        return color;

    }
}

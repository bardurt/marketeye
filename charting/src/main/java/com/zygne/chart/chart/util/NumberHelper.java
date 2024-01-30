package com.zygne.chart.chart.util;

public class NumberHelper {
    private static final int BILLION = 1000000000;
    private static final int MILLION = 1000000;
    private static final int THOUSAND = 1000;

    public static String getShortName(double original){

        if(original >= BILLION){
            double firstPart = (original / (double) BILLION);
            return String.format("%.2f", firstPart)+ " B";
        } else if(original >= MILLION){
            double firstPart = (original / (double) MILLION);
            return String.format("%.2f", firstPart)+ " M";
        }  else if(original >= THOUSAND){
            double firstPart = (original / (double) THOUSAND);
            return String.format("%.2f", firstPart)+ " K";
        }

        return "" + original;
    }
}

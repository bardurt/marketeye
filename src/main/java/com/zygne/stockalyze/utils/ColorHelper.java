package com.zygne.stockalyze.utils;

public class ColorHelper {


    public static String getColorForStrength(double strength, double cutoff) {

        if (strength < cutoff) {
            return null;
        }

        String color = "";

        if (strength > 75) {
            color = "#FFD58B";
        }

        if (strength > 80) {
            color = "#E5FF8B";
        }

        if (strength > 90) {
            color = "#70FF6A";
        }

        if (strength > 95) {
            color = "#3ADB33";
        }

        return color;
    }

    public static String getColorForPercentile(double percentile){

        String color = null;

        if(percentile < 4){
            color = "#FFD175";
        }

        if (percentile < 3) {
            color = "#FFB275";
        }

        if (percentile < 2) {
            color = "#FF9075";
        }

        if (percentile < 1) {
            color = "#FF7175";
        }

        if(percentile < 0.5){
            color = "FF7575";
        }

        return color;

    }
}

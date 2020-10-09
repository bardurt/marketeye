package com.zygne.stockalyze.utils;

public class StringUtils {


    public static String center(String source, int width){

        int padding = (width - source.length()) / 2;


        return " ";
    }

    public static String repeat(String source, int amount){

        StringBuilder builder = new StringBuilder();


        return builder.toString();
    }

    public static boolean isInteger(String value){

        boolean valid = true;
        try {
            Integer.parseInt(value);
        } catch (Exception e){
            valid = false;
        }

        return valid;
    }

    public static boolean idDouble(String value){

        boolean valid = true;
        try {
            Double.parseDouble(value);
        } catch (Exception e){
            valid = false;
        }

        return valid;
    }
}

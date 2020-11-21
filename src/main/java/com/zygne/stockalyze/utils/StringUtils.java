package com.zygne.stockalyze.utils;

public class StringUtils {

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

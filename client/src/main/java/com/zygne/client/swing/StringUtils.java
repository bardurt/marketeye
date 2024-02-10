package com.zygne.client.swing;

public class StringUtils {

    public static String repeatAndPad(String value, int count, int maxCount){
        if(count <= 0){
            return value;
        }

        if(value.isEmpty()){
            return value;
        }

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(value.repeat(count));

        if(count < maxCount){
            stringBuilder.append(" ".repeat(maxCount - count));
        }

        return stringBuilder.toString();
    }
}

package com.zygne.stockanalyzer.domain.utils;

public class TagHelper {

    public static String getValueFromTagName(String name, String raw) {
        if(name == null || name.isEmpty()){
            return "";
        }

        if(raw == null || raw.isEmpty()){
            return "";
        }

        String startTag = "<" + name + ">";
        String endTarget = "</" + name + ">";

        String[] firstParts = raw.split(endTarget, -1);

        // if size is not 2, then one of the following error has happened
        // 1 : end tag does not exists in the raw data
        // 2 : end tag appears more than 1 time.
        // both of these cases makes the XML invalid, so return empty
        if (firstParts.length != 2) {
            return "";
        }

        String[]  secondParts = firstParts[0].split(startTag, -1);

        // if size is not 2, then one of the following error has happened
        // 1 : start tag does not exists in the raw data
        // 2 : start tag appears more than 1 time.
        // both of these cases makes the XML invalid, so return empty
        if (secondParts.length != 2) {
            return "";
        }

        return secondParts[1];
    }

    public static String createTag(String name, String value){
        String startTag = "<" + name + ">";
        String endTag = "</" + name + ">";

        return startTag+value+endTag;
    }

    public static String start(String name){
        String startTag = "<" + name + ">";

        return startTag;
    }

    public static String end(String name){
        String startTag = "</" + name + ">";

        return startTag;
    }
}

package com.zygne.stockalyze.domain.utils;

public class FolderHelper {

    public static String getLatestCachedFolder(){

        return Constants.CACHE_FOLDER + "/" + "DAY_"+TimeHelper.getDayOfYear();
    }
}

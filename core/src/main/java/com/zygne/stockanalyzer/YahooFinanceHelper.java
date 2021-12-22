package com.zygne.stockanalyzer;

public class YahooFinanceHelper {


    public static final String REGULAR_MARKET_PRICE = "regularMarketPrice";
    public static final String CURRENT_PRICE = "currentPrice";

    private static final String currentPriceTag = "\"currentPrice\":\\{\"raw\":";
    private static final String regularMarketPriceTag = "\"regularMarketPrice\":\\{\"raw\":";

    public static double getCurrentPrice(String pageSource){
        double currentPrice = 0.0d;
        boolean success = false;

        try {
            String[] sections = pageSource.split(currentPriceTag);
            String[] sections1 = sections[1].split(",");
            currentPrice = Double.parseDouble(sections1[0]);
            success = true;
        } catch (Exception e){
            success = false;
        }

        if(!success){
            try {
                String[] sections = pageSource.split(regularMarketPriceTag);
                String[] sections1 = sections[1].split(",");
                currentPrice = Double.parseDouble(sections1[0]);
                success = true;
            } catch (Exception e){
                success = false;
            }
        }

        return currentPrice;
    }

}

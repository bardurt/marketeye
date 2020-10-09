package com.zygne.stockalyze.domain.interactor.implementation.data;

import com.zygne.stockalyze.domain.MainThread;
import com.zygne.stockalyze.domain.executor.Executor;
import com.zygne.stockalyze.domain.interactor.base.BaseInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.data.base.MarketPriceInteractor;

import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class YahooMarketPriceInteractor extends BaseInteractor implements MarketPriceInteractor {

    private static final String PRICE_TAG = "regularMarketPrice";
    private Callback callback;
    private String ticker;

    public YahooMarketPriceInteractor(Executor executor, MainThread mainThread, Callback callback, String ticker) {
        super(executor, mainThread);
        this.callback = callback;
        this.ticker = ticker;
    }

    @Override
    public void run() {
        ticker = ticker.toUpperCase();

        String url = "https://finance.yahoo.com/quote/"+ticker +"/key-statistics?p="+ticker;


        String floatString = null;
        try {
            URL content = new URL(url);
            InputStream stream = content.openStream();

            Scanner inputStream = new Scanner(stream);
            while (inputStream.hasNext()) {

                String data = inputStream.next();

                if(data.contains(PRICE_TAG)){
                    floatString = data;
                    break;
                }
            }
            inputStream.close();
        } catch (Exception e){
            return;
        }

        if(floatString != null){
            double priceFromRaw = getPriceFromRaw(floatString);

            mainThread.post(new Runnable() {
                @Override
                public void run() {
                    callback.onMarketPriceFetched(priceFromRaw);
                }
            });

        } else {
            mainThread.post(new Runnable() {
                @Override
                public void run() {
                    callback.onMarketPriceFetched(-1);
                }
            });
        }
    }


    private double getPriceFromRaw(String raw){
        String[] parts = raw.split(",", -1);

        String floatString = "";

        for(String s : parts){
            if(s.contains(PRICE_TAG)){
                floatString = s;
                break;
            }
        }

        double price = -1;

        try {
            price = Double.parseDouble(floatString.split(":")[2]);
        } catch (Exception e){
            System.out.println("Error getting float for stock : " + ticker);
        }

        return price;
    }
}

package com.zygne.data;

import com.zygne.arch.domain.Logger;
import com.zygne.data.domain.DataBroker;
import com.zygne.data.domain.FinanceData;
import com.zygne.data.domain.model.BarData;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class CryptoDataBroker implements DataBroker {

    private Callback callback;


    private final Logger logger;

    public CryptoDataBroker(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void downloadData(String symbol, String interval, int yearsBack) {

        final String url = "https://www.cryptodatadownload.com/cdd/Bitstamp_" + symbol.toUpperCase() + "USD_d.csv";
        logger.log(Logger.LOG_LEVEL.INFO, "Downloading data for Crypto Currency : " + symbol);

        Thread t = new Thread(() -> {
            List<FinanceData> data = downLoadTimeSeries(url);
            if (callback != null) {
                callback.onDataFinished(data);
            }
        });

        t.start();
    }


    private List<FinanceData> downLoadTimeSeries(String url) {

        List<FinanceData> lines = new java.util.ArrayList<>();

        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        java.net.URLConnection urlConnection;
        try {
            java.net.URL content = new java.net.URL(url);

            urlConnection = content.openConnection();

            inputStreamReader = new InputStreamReader(urlConnection.getInputStream());

            bufferedReader = new BufferedReader(inputStreamReader);

            String line;

            int count = 0;
            while ((line = bufferedReader.readLine()) != null) {
                count++;
                if (count > 2) {
                    var barData = BarData.fromStreamBitstamp(line);
                    if (barData != null) {
                        lines.add(barData);
                    }
                }

            }

        } catch (Exception e) {
            logger.log(Logger.LOG_LEVEL.INFO, "Unable to fetch data");
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }
            }

            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return lines;
    }

    @Override
    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void removeCallback() {
        this.callback = null;
    }

}
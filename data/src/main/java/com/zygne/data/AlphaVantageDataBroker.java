package com.zygne.data;

import com.zygne.arch.domain.Logger;
import com.zygne.data.domain.DataBroker;
import com.zygne.data.domain.FinanceData;
import com.zygne.data.domain.model.BarData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class AlphaVantageDataBroker implements DataBroker {

    private final String apiKey;
    private Callback callback;
    private final Logger logger;

    public AlphaVantageDataBroker(Logger logger, String api) {
        this.logger = logger;
        this.apiKey = api;
    }

    @Override
    public void downloadData(String symbol, String interval, int yearsBack) {

        logger.log(Logger.LOG_LEVEL.INFO, symbol + " " + yearsBack + " years");

        final String url = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=" +
                symbol +
                "&apikey=" +
                apiKey +
                "&outputsize=full&datatype=csv";

        System.out.println(url);
        logger.log(Logger.LOG_LEVEL.INFO, "Downloading data for " + symbol);

        Thread t = new Thread(() -> {
            if (apiKey.isEmpty()) {
                if (callback != null) {
                    callback.onDataFinished(new ArrayList<>());
                }
            } else {
                List<FinanceData> data = downLoadTimeSeries(url);
                if (callback != null) {
                    callback.onDataFinished(data);
                }
            }
        });

        t.start();
    }

    private List<FinanceData> downLoadTimeSeries(String url) {

        List<FinanceData> lines = new ArrayList<>();

        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        URLConnection urlConnection;
        try {
            URL content = new URL(url);

            urlConnection = content.openConnection();

            inputStreamReader = new InputStreamReader(urlConnection.getInputStream());

            bufferedReader = new BufferedReader(inputStreamReader);

            String line;

            int count = 0;
            while ((line = bufferedReader.readLine()) != null) {
                count++;
                if (count > 1) {
                    BarData barData = BarData.fromStreamAlphaVantage(line);
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
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
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
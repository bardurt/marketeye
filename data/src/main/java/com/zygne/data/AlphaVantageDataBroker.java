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
import java.util.Objects;


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


        String url = "";

        if(Objects.equals(symbol, "wti")){
            url = "https://www.alphavantage.co/query?function=WTI&interval=daily&apikey=" + apiKey + "&datatype=csv";
        } else {
            url = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=" + symbol + "&apikey=" + apiKey + "&outputsize=full&datatype=csv";
        }

        System.out.println(url);
        logger.log(Logger.LOG_LEVEL.INFO, "Downloading data for " + symbol);

        String finalUrl = url;
        Thread t = new Thread(() -> {
            List<FinanceData> data = downLoadTimeSeries(finalUrl);
            if (callback != null) {
                callback.onDataFinished(data);
            }
        });

        t.start();
    }


    public void testData() {


        String url = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=" + "spx" + "&apikey=" + apiKey + "&outputsize=full&datatype=csv";


        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        URLConnection urlConnection;
        try {
            URL content = new URL(url);

            // establish connection to file in URL
            urlConnection = content.openConnection();

            inputStreamReader = new InputStreamReader(urlConnection.getInputStream());

            bufferedReader = new BufferedReader(inputStreamReader);

            String line;

            while ((line = bufferedReader.readLine()) != null) {

                System.out.println(line);

            }

        } catch (Exception e) {
            e.printStackTrace();
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

    }

    private List<FinanceData> downLoadTimeSeries(String url) {

        List<FinanceData> lines = new ArrayList<>();

        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        URLConnection urlConnection;
        try {
            URL content = new URL(url);

            // establish connection to file in URL
            urlConnection = content.openConnection();

            inputStreamReader = new InputStreamReader(urlConnection.getInputStream());

            bufferedReader = new BufferedReader(inputStreamReader);

            String line;

            int count = 0;
            while ((line = bufferedReader.readLine()) != null) {
                count++;
                if(count > 1) {
                    BarData barData = BarData.fromStreamAlphaVantage(line);
                    if (barData != null) {
                        lines.add(barData);
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
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
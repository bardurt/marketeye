package com.zygne.data;

import com.zygne.data.domain.DataBroker;
import com.zygne.data.domain.model.BarData;
import com.zygne.arch.domain.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class YahooDataBroker implements DataBroker {

    private Callback callback;
    private final Logger logger;

    public YahooDataBroker(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void downloadHistoricalBarData(String symbol, int yearsBack) {

        logger.log(Logger.LOG_LEVEL.INFO, symbol + " " + yearsBack + " years");

        Calendar calendar = Calendar.getInstance();
        String timeEnd = "" + (calendar.getTime().getTime() / 1000);

        calendar.add(Calendar.YEAR, yearsBack * -1);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        String timeStart = "" + (calendar.getTime().getTime() / 1000);

        String time = "1d";

        String url = "https://query1.finance.yahoo.com/v7/finance/download/" + symbol + "?period1=" + timeStart + "&period2=" + timeEnd + "&interval=" + time + "&events=history&includeAdjustedClose=true";
        System.out.println(url);
        logger.log(Logger.LOG_LEVEL.INFO, "Downloading data for " + symbol);

        Thread t = new Thread(() -> {
            List<BarData> data = downLoadTimeSeries(url);
            if (callback != null) {
                callback.onDataFinished(data);
            }
        });

        t.start();
    }

    private List<BarData> downLoadTimeSeries(String url) {

        List<BarData> lines = new ArrayList<>();

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

                BarData barData = BarData.fromStream(line, BarData.DataFarm.YAHOO);
                if (barData != null) {
                    barData.setDataFarm(BarData.DataFarm.YAHOO);
                    lines.add(barData);
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

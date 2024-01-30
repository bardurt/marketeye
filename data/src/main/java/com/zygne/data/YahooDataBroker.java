package com.zygne.data;

import com.zygne.data.domain.DataBroker;
import com.zygne.data.domain.model.BarData;
import com.zygne.data.domain.model.DataSize;
import com.zygne.data.domain.model.enums.TimeInterval;
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
    public void downloadHistoricalBarData(String symbol, DataSize dataSize, TimeInterval timeInterval, boolean yearStart) {

        logger.log(Logger.LOG_LEVEL.INFO, symbol + " " + timeInterval + " " + dataSize.getSize() + " " + dataSize.getUnit());

        final int years = dataSize.getSize();

        Calendar calendar = Calendar.getInstance();
        String timeEnd = "" + (calendar.getTime().getTime() / 1000);

        if(dataSize.getUnit() == DataSize.Unit.Year) {
            calendar.add(Calendar.YEAR, years * -1);
        } else if(dataSize.getUnit() == DataSize.Unit.Month){
            calendar.add(Calendar.MONTH, years * -1);
        } else {
            calendar.add(Calendar.YEAR, years * -1);
        }

        if(yearStart){
            calendar.set(Calendar.MONTH, 0);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
        }

        String timeStart = "" + (calendar.getTime().getTime() / 1000);

        String time = "1d";

        if(timeInterval == TimeInterval.Day){
            time = "1d";
        } else  if(timeInterval == TimeInterval.Week){
            time = "1wk";
        } else  if(timeInterval == TimeInterval.Month){
            time = "1mo";
        }

        final String name = symbol;


        String url = "https://query1.finance.yahoo.com/v7/finance/download/" + name + "?period1=" + timeStart + "&period2=" + timeEnd + "&interval=" + time + "&events=history&includeAdjustedClose=true";
        System.out.println(url);
        logger.log(Logger.LOG_LEVEL.INFO, "Downloading data for " + name);

        Thread t = new Thread(() -> {
            List<BarData> data = downLoadTimeSeries(url);
            if(callback != null){
                callback.onDataFinished(data);
            }
        });

        t.start();
    }

    private List<BarData> downLoadTimeSeries(String url) {

        List<BarData> lines = new ArrayList<>();

        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        URLConnection urlConnection = null;
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


    public static void main(String[] args){

        DataBroker dataBroker = new YahooDataBroker(new Logger() {
            @Override
            public void shutDown() {

            }

            @Override
            public void setUp() {

            }

            @Override
            public void log(LOG_LEVEL level, String message) {

            }

            @Override
            public void clear() {

            }
        });

    }

}

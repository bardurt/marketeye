package com.zygne.data;

import com.zygne.arch.domain.Logger;
import com.zygne.data.domain.DataBroker;
import com.zygne.data.domain.FinanceData;
import com.zygne.data.domain.model.CotData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class CotDataBroker implements DataBroker {

    private Callback callback;
    private final Logger logger;

    public CotDataBroker(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void downloadData(String symbol, String interval, int yearsBack) {

        logger.log(Logger.LOG_LEVEL.INFO, symbol + " " + yearsBack + " years");

        System.out.println(symbol);
        logger.log(Logger.LOG_LEVEL.INFO, "Downloading data for " + symbol);

        Thread t = new Thread(() -> {
            List<FinanceData> data = downloadData(symbol);
            if (callback != null) {
                callback.onDataFinished(data);
            }
        });

        t.start();
    }

    private List<FinanceData> downloadData(String url) {

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

            while ((line = bufferedReader.readLine()) != null) {
                CotData d = CotData.fromStream(line);
                lines.add(d);
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

    public static void main(String[] args) {

        CotDataBroker cotDataBroker = new CotDataBroker(new Logger() {
            @Override
            public void shutDown() {

            }

            @Override
            public void setUp() {

            }

            @Override
            public void log(LOG_LEVEL level, String message) {
                System.out.println(message);
            }

            @Override
            public void clear() {

            }
        });

        cotDataBroker.downloadData("","", 1);
    }

}

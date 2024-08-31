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

public class CftcCotDataBroker implements DataBroker {

    private Callback callback;
    private final Logger logger;

    private static List<String> ITEMS = new ArrayList<>() {
        {
            add("WHEAT-SRW - CHICAGO BOARD OF TRADE");
            add("BITCOIN - CHICAGO MERCANTILE EXCHANGE");
            add("E-MINI S&P 500 - CHICAGO MERCANTILE EXCHANGE");
            add("NASDAQ MINI - CHICAGO MERCANTILE EXCHANGE");
            add("GOLD - COMMODITY EXCHANGE INC.");
            add("SILVER - COMMODITY EXCHANGE INC.");
            add("USD INDEX - ICE FUTURES U.S.");
            add("DJIA Consolidated - CHICAGO BOARD OF TRADE");
            add("WTI FINANCIAL CRUDE OIL - NEW YORK MERCANTILE EXCHANGE");
            add("CORN - CHICAGO BOARD OF TRADE");
        }
    };

    public CftcCotDataBroker(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void downloadData(String symbol, String interval, int yearsBack) {

        logger.log(Logger.LOG_LEVEL.INFO, symbol + " " + yearsBack + " years");

        System.out.println(symbol);
        logger.log(Logger.LOG_LEVEL.INFO, "Downloading data for " + symbol);

        Thread t = new Thread(() -> {
            List<FinanceData> data = downloadData("https://www.cftc.gov/dea/newcot/deafut.txt");
            if (callback != null) {
                callback.onDataFinished(data);
            }
        });

        t.start();
    }

    private List<FinanceData> downloadData(String url) {


        FileWriter fileWriter = new FileWriter("cftc_cot.txt");

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
                String[] parts = line.split(",");

                if (ITEMS.contains(parts[0].replace("\"", ""))) {
                    String lineItem = parts[1] + "|" + parts[0].replace("\"", "") + "|" + parts[11].trim() + "|" + parts[12].trim();
                    fileWriter.writeLine(lineItem);
                }
            }

            fileWriter.close();

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

        CftcCotDataBroker cotDataBroker = new CftcCotDataBroker(new Logger() {
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

        cotDataBroker.downloadData("", "", 1);
    }

}

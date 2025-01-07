package com.zygne.data;

import com.zygne.data.domain.DataBroker;
import com.zygne.data.domain.FinanceData;
import com.zygne.data.domain.model.BarData;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class CryptoDataBroker implements DataBroker {

    private Callback callback;


    public CryptoDataBroker() {
    }

    @Override
    public void downloadData(String symbol, String interval, int yearsBack) {

        final String url = "https://www.cryptodatadownload.com/cdd/Bitstamp_" + symbol.toUpperCase() + "USD_d.csv";

        System.out.println(url);

        Thread t = new Thread(() -> {
            List<FinanceData> data = downLoadTimeSeries(url);
            if (callback != null) {
                callback.onDataFinished(data);
            }
        });

        t.start();
    }


    public void testData() {


        String url = "https://www.cryptodatadownload.com/cdd/Bitstamp_ETHUSD_d.csv";

        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        java.net.URLConnection urlConnection;
        try {
            java.net.URL content = new java.net.URL(url);

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

    }

    private List<FinanceData> downLoadTimeSeries(String url) {

        List<FinanceData> lines = new java.util.ArrayList<>();

        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        java.net.URLConnection urlConnection;
        try {
            java.net.URL content = new java.net.URL(url);

            // establish connection to file in URL
            urlConnection = content.openConnection();

            inputStreamReader = new InputStreamReader(urlConnection.getInputStream());

            bufferedReader = new BufferedReader(inputStreamReader);

            String line;

            int count = 0;
            while ((line = bufferedReader.readLine()) != null) {
                count++;
                if(count > 2) {
                    var barData = BarData.fromStreamBitstamp(line);
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

    public static void main(String[] args){

        CryptoDataBroker dataBroker = new CryptoDataBroker();

        dataBroker.testData();
    }

}
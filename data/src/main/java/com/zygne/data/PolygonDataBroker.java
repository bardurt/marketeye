package com.zygne.data;

import com.google.gson.Gson;
import com.zygne.arch.domain.Logger;
import com.zygne.data.domain.DataBroker;
import com.zygne.data.domain.FinanceData;
import com.zygne.data.domain.model.BarData;
import com.zygne.data.domain.model.PolygonData;
import com.zygne.data.domain.model.PolygonResponse;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PolygonDataBroker implements DataBroker {
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    private final String apiKey;
    private Callback callback;
    private final Logger logger;

    DateFormat df = new SimpleDateFormat(DATE_FORMAT);

    public PolygonDataBroker(Logger logger, String apiKey) {
        this.logger = logger;
        this.apiKey = apiKey;
    }

    @Override
    public void downloadData(String symbol, String interval, int yearsBack) {

        Calendar c = Calendar.getInstance();
        String end = df.format(c.getTimeInMillis());

        c.add(Calendar.YEAR, -10);
        String start = df.format(c.getTimeInMillis());

        final String url = "https://api.polygon.io/v2/aggs/ticker/" +
                symbol.toUpperCase() +
                "/range/1/day/" +
                start +
                "/" +
                end +
                "?adjusted=true&sort=desc&apiKey=" +
                apiKey;

        logger.log(Logger.LOG_LEVEL.INFO, "Downloading data from Polygon : " + symbol);

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

        List<FinanceData> lines = new java.util.ArrayList<>();

        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        java.net.URLConnection urlConnection;

        StringBuilder stringBuilder = new StringBuilder();
        try {
            java.net.URL content = new java.net.URL(url);

            urlConnection = content.openConnection();

            inputStreamReader = new InputStreamReader(urlConnection.getInputStream());

            bufferedReader = new BufferedReader(inputStreamReader);


            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
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

        String result = stringBuilder.toString();

        PolygonResponse response = new Gson().fromJson(result, PolygonResponse.class);

        if (response == null) {
            return lines;
        }

        if (response.data == null) {
            return lines;
        }

        Calendar calendar = Calendar.getInstance();
        for (PolygonData data : response.data) {
            calendar.setTimeInMillis(data.timeStamp);

            String date = df.format(calendar.getTimeInMillis());
            BarData barData = new BarData(date, data.open, data.high, data.low, data.close, (long) data.volume);
            lines.add(barData);
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
package com.zygne.stockanalyzer;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.zygne.stockanalyzer.domain.Logger;
import com.zygne.stockanalyzer.domain.api.DataBroker;
import com.zygne.stockanalyzer.domain.model.BarData;
import com.zygne.stockanalyzer.domain.model.DataSize;
import com.zygne.stockanalyzer.domain.model.enums.TimeInterval;
import com.zygne.stockanalyzer.domain.utils.TimeHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class CryptoCompareDataBroker implements DataBroker {

    private Callback callback;
    private final Logger logger;
    private String apiKey = "";

    private static final int WEEK = 169;
    private static final int MONTH = 720;
    private static final int HALF_YEAR = 4320;
    private static final int YEAR = 8640;

    private static final int BATCH = 2000;

    public CryptoCompareDataBroker(String apiKey, Logger logger) {
        this.apiKey = apiKey;
        this.logger = logger;
    }

    @Override
    public void getLastTickPrice(String symbol) {

    }

    @Override
    public void downloadHistoricalBarData(String symbol, DataSize dataSize, TimeInterval timeInterval) {
        int limit = YEAR;

        if (dataSize.getUnit() == DataSize.Unit.Month) {
            limit = MONTH * dataSize.getSize();
        } else if (dataSize.getUnit() == DataSize.Unit.Year) {
            limit = YEAR * dataSize.getSize();
        }

        if(dataSize.getSize() == 0){
            limit = MONTH;
        }

        logger.log(Logger.LOG_LEVEL.INFO, "Will download  : " + symbol + " " + dataSize.getSize() + " " + dataSize.getUnit());
        int count = 0;

        long toT = System.currentTimeMillis() / 1000;

        List<Data> dataList = new ArrayList<>();

        while (count < limit) {

            logger.log(Logger.LOG_LEVEL.INFO, "Downloading batch [ " + count + " - " + (count+ BATCH) + "] of " + limit);
            String url = "https://min-api.cryptocompare.com/data/v2/histohour?fsym=" + symbol + "&tsym=USD&limit=" + BATCH + "&toTs=" + toT + "&api_key=" + apiKey;

            logger.log(Logger.LOG_LEVEL.INFO, "Downloading : " + url);
            StringBuilder body = new StringBuilder();

            InputStreamReader inputStreamReader = null;
            BufferedReader bufferedReader = null;
            URLConnection urlConnection = null;

            try {
                URL content = new URL(url);

                urlConnection = content.openConnection();

                inputStreamReader = new InputStreamReader(urlConnection.getInputStream());

                bufferedReader = new BufferedReader(inputStreamReader);

                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    body.append(line);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (inputStreamReader != null) {
                    try {
                        inputStreamReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            Gson gson = new Gson();

            Data data = gson.fromJson(body.toString(), Data.class);

            dataList.add(data);
            toT = data.dataWrapper.timeFrom;

            count += data.dataWrapper.tickData.size();

        }

        List<BarData> barDataList = new ArrayList<>();
        for(Data d : dataList){
            for(TickData t : d.dataWrapper.tickData){

                BarData b = new BarData(TimeHelper.getDateFromTimeStamp(t.timeStamp *1000), t.open, t.high, t.low, t.close, (long) t.volume);
                b.setDataFarm(BarData.DataFarm.CRYPTO_COMPARE);
                barDataList.add(b);
            }
        }


        callback.onDataFinished(barDataList);
    }

    @Override
    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void removeCallback() {
        this.callback = null;
    }

    @Override
    public void setAsset(Asset asset) {

    }

    @Override
    public void connect() {

    }

    @Override
    public void disconnect() {

    }

    @Override
    public void setConnectionListener(ConnectionListener connectionListener) {

    }

    @Override
    public void removeConnectionListener() {

    }

    public static class Data {
        @SerializedName("Response")
        private String response;
        @SerializedName("Message")
        private String message;
        @SerializedName("hasWarnings")
        private boolean hasWarnings;
        @SerializedName("Type")
        private int type;
        @SerializedName("Data")
        private DataWrapper dataWrapper;

    }

    public static class DataWrapper {
        @SerializedName("Aggregated")
        private boolean aggregated;
        @SerializedName("TimeFrom")
        private long timeFrom;
        @SerializedName("TimeTo")
        private long timeTo;
        @SerializedName("Data")
        private ArrayList<TickData> tickData;

    }

    public static class TickData {

        @SerializedName("high")
        private double high;
        @SerializedName("low")
        private double low;
        @SerializedName("open")
        private double open;
        @SerializedName("close")
        private double close;
        @SerializedName("volumeto")
        private double volume;
        @SerializedName("time")
        private long timeStamp;

        public double getHigh() {
            return high;
        }

        public void setHigh(double high) {
            this.high = high;
        }

        public double getLow() {
            return low;
        }

        public void setLow(double low) {
            this.low = low;
        }

        public double getOpen() {
            return open;
        }

        public void setOpen(double open) {
            this.open = open;
        }

        public double getClose() {
            return close;
        }

        public void setClose(double close) {
            this.close = close;
        }

        public double getVolume() {
            return volume;
        }

        public void setVolume(double volume) {
            this.volume = volume;
        }

        public long getTimeStamp() {
            return timeStamp;
        }

        public void setTimeStamp(long timeStamp) {
            this.timeStamp = timeStamp;
        }
    }
}

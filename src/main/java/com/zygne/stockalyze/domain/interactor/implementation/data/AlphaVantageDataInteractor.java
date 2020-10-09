package com.zygne.stockalyze.domain.interactor.implementation.data;

import com.zygne.stockalyze.domain.MainThread;
import com.zygne.stockalyze.domain.executor.Executor;
import com.zygne.stockalyze.domain.interactor.base.BaseInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.data.base.DataFetchInteractor;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AlphaVantageDataInteractor extends BaseInteractor implements DataFetchInteractor {

    private static final String API_KEY = "KZ4XNT2FTNPZGSOM";
    private static final String INTERVAL = "1min";

    private Callback callback;
    private String ticker;


    public AlphaVantageDataInteractor(Executor executor, MainThread mainThread, Callback callback, String ticker) {
        super(executor, mainThread);
        this.callback = callback;
        this.ticker = ticker;
    }


    @Override
    public void run() {

        int year = 1;
        int month = 1;

        long start = System.currentTimeMillis();

        List<String> lines = new ArrayList<>();
        while (year < 3){

            while (month < 13){

                String slice = "year"+year+"month"+month;

                String url = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY_EXTENDED&symbol=" + ticker.toUpperCase() + "&interval=" + INTERVAL + "&slice=" + slice + "&apikey=KZ4XNT2FTNPZGSOM";

                try {
                    URL content = new URL(url);
                    InputStream stream = content.openStream();

                    Scanner inputStream = new Scanner(stream);
                    while (inputStream.hasNext()) {
                        String data = splitString(inputStream.next());
                        if(data != null) {
                           lines.add(data);
                        }
                    }
                    inputStream.close();
                } catch (Exception e) {
                    return;
                }
                month++;
            }

            year++;
        }

        if (lines.isEmpty()) {
            mainThread.post(() -> callback.onDataFetchError("Could not fetch data from : Alpha Vantage" ));

            return;
        }

        long runtime = System.currentTimeMillis() - start;

        System.out.println("AlphaVantage rt : " + runtime + " ms");
        System.out.println("Data size " + lines.size());

        mainThread.post(() -> callback.onDataFetched(lines, ticker));
    }

    private String splitString(String line){

        String[] parts = line.split(",",-1);

        if(parts[0].equalsIgnoreCase("time")){
            return null;
        }

        if(parts.length < 5){
            return null;
        }

        return line;
    }

}

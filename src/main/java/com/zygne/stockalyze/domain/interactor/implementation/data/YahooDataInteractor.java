package com.zygne.stockalyze.domain.interactor.implementation.data;

import com.zygne.stockalyze.domain.MainThread;
import com.zygne.stockalyze.domain.executor.Executor;
import com.zygne.stockalyze.domain.interactor.base.BaseInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.data.base.DataFetchInteractor;

import java.io.InputStream;
import java.net.URL;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class YahooDataInteractor extends BaseInteractor implements DataFetchInteractor {

    private static final int END_TIME = 1;
    private static final int START_TIME = 1826;

    private final Callback callback;
    private String ticker;

    public YahooDataInteractor(Executor executor, MainThread mainThread, Callback callback, String ticker) {
        super(executor, mainThread);
        this.callback = callback;
        this.ticker = ticker;
    }


    @Override
    public void run() {
        Instant now = Instant.now();
        Instant yesterday = now.minus(END_TIME, ChronoUnit.DAYS);
        Instant fiveYears = now.minus(START_TIME, ChronoUnit.DAYS);

        String timeTo = "" + yesterday.getEpochSecond();
        String timeFrom = "" + fiveYears.getEpochSecond();

        ticker = ticker.toUpperCase();
        String url = "https://query1.finance.yahoo.com/v7/finance/download/" + ticker + "?period1=" + timeFrom + "&period2=" + timeTo + "&interval=1d&events=history";

        List<String> lines = new ArrayList<>();
        try {
            URL content = new URL(url);
            InputStream stream = content.openStream();

            Scanner inputStream = new Scanner(stream);
            int count = 0;
            while (inputStream.hasNext()) {

                String data = cleanUp(inputStream.next());

                if (count > 1) {
                    if(data != null) {
                        lines.add(data);
                    }
                }

                count++;
            }
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            mainThread.post(() -> callback.onDataFetchError("Could not fetch data from : " + url));
            return;
        }

        if (lines.isEmpty()) {
            mainThread.post(() -> callback.onDataFetchError("Could not fetch data from : " + url));

            return;
        }

        mainThread.post(() -> callback.onDataFetched(lines, ticker));

    }

    private String cleanUp(String source){

        String[] parts = source.split(",", -1);

        if(parts.length >= 7) {
            return parts[0] + "," + parts[1] + "," + parts[2] + "," + parts[3] + "," + parts[4] + "," + parts[6];
        } else {
            return null;
        }
    }

}

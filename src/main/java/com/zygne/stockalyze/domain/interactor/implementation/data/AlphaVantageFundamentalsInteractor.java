package com.zygne.stockalyze.domain.interactor.implementation.data;

import com.google.gson.Gson;
import com.zygne.stockalyze.domain.executor.MainThread;
import com.zygne.stockalyze.domain.executor.Executor;
import com.zygne.stockalyze.domain.interactor.base.BaseInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.data.base.FundamentalsInteractor;
import com.zygne.stockalyze.domain.model.Fundamentals;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class AlphaVantageFundamentalsInteractor extends BaseInteractor implements FundamentalsInteractor {
    private final Callback callback;
    private final String ticker;
    private final String apiKey;

    public AlphaVantageFundamentalsInteractor(Executor executor, MainThread mainThread, Callback callback, String ticker, String apiKey) {
        super(executor, mainThread);
        this.callback = callback;
        this.ticker = ticker;
        this.apiKey = apiKey;
    }

    @Override
    public void run() {

        String url = "https://www.alphavantage.co/query?function=OVERVIEW&symbol=" + ticker + "&apikey=" + apiKey;

        StringBuilder body = new StringBuilder();
        try {
            URL content = new URL(url);

            // establish connection to file in URL
            URLConnection urlConn = content.openConnection();

            // ...
            InputStreamReader inputCSV = new InputStreamReader(
                    urlConn.getInputStream());
            // ...
            BufferedReader br = new BufferedReader(inputCSV);

            String line;

            while ((line = br.readLine()) != null) {
                body.append(line);
            }

            br.close();
            inputCSV.close();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }


        Gson gson = new Gson();

        Fundamentals fundamentals = gson.fromJson(body.toString(), Fundamentals.class);

        mainThread.post(() -> callback.onFundamentalsFetched(fundamentals));

    }
}

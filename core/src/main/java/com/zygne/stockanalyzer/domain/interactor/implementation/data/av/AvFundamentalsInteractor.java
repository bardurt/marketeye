package com.zygne.stockanalyzer.domain.interactor.implementation.data.av;

import com.google.gson.Gson;
import com.zygne.stockanalyzer.domain.executor.Executor;
import com.zygne.stockanalyzer.domain.executor.MainThread;
import com.zygne.stockanalyzer.domain.interactor.base.BaseInteractor;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.base.FundamentalsInteractor;
import com.zygne.stockanalyzer.domain.model.Fundamentals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class AvFundamentalsInteractor extends BaseInteractor implements FundamentalsInteractor {
    private final Callback callback;
    private final String ticker;
    private final String apiKey;

    public AvFundamentalsInteractor(Executor executor, MainThread mainThread, Callback callback, String ticker, String apiKey) {
        super(executor, mainThread);
        this.callback = callback;
        this.ticker = ticker;
        this.apiKey = apiKey;
    }

    @Override
    public void run() {

        String url = "https://www.alphavantage.co/query?function=OVERVIEW&symbol=" + ticker + "&apikey=" + apiKey;

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

        Fundamentals fundamentals = gson.fromJson(body.toString(), Fundamentals.class);

        fundamentals.calculate();

        mainThread.post(() -> callback.onFundamentalsFetched(fundamentals));

    }
}

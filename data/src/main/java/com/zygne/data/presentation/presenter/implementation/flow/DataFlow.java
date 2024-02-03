package com.zygne.data.presentation.presenter.implementation.flow;

import com.zygne.data.domain.DataBroker;
import com.zygne.data.domain.interactor.implementation.data.*;
import com.zygne.data.domain.interactor.implementation.data.base.*;
import com.zygne.data.domain.interactor.implementation.data.io.*;
import com.zygne.data.domain.model.BarData;
import com.zygne.data.domain.model.Histogram;
import com.zygne.arch.domain.Logger;
import com.zygne.arch.domain.executor.Executor;
import com.zygne.arch.domain.executor.MainThread;

import java.util.ArrayList;
import java.util.List;

public class DataFlow implements DataFetchInteractor.Callback,
        HistogramInteractor.Callback {


    private final Callback callback;
    private final Executor executor;
    private final MainThread mainThread;
    private Logger logger;
    private String ticker;
    private final List<BarData> downloadedData = new ArrayList<>();


    public DataFlow(Executor executor, MainThread mainThread, Callback callback, Logger logger) {
        this.callback = callback;
        this.executor = executor;
        this.mainThread = mainThread;
        this.logger = logger;
    }

    public void fetchData(DataBroker dataBroker, String ticker, int years) {
        this.ticker = ticker;
        this.downloadedData.clear();

        logger.log(Logger.LOG_LEVEL.INFO, "Download data for " + ticker.toUpperCase());
        new DataFetchInteractorImpl(executor, mainThread, this, ticker, years, dataBroker).execute();
    }


    @Override
    public void onDataFetched(List<BarData> entries, String timestamp) {
        downloadedData.addAll(entries);
        logger.log(Logger.LOG_LEVEL.INFO, "Generating histogram");
        new HistogramInteractorImpl(executor, mainThread, this, downloadedData).execute();
    }

    @Override
    public void onDataFetchError(String message) {
        callback.onDataError();
    }

    @Override
    public void onStatusUpdate(String message) {
    }

    @Override
    public void onHistogramCreated(List<Histogram> data) {
        String dateRange = data.get(data.size() - 1).dateTime + " - " + data.get(0).dateTime;
        logger.log(Logger.LOG_LEVEL.INFO, "Complete");
        callback.onDataFetched(data, dateRange);
    }

    public interface Callback {
        void onDataFetched(List<Histogram> data, String time);

        void onDataError();
    }
}

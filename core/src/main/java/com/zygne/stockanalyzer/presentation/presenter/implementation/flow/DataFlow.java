package com.zygne.stockanalyzer.presentation.presenter.implementation.flow;

import com.zygne.stockanalyzer.domain.Logger;
import com.zygne.stockanalyzer.domain.api.DataBroker;
import com.zygne.stockanalyzer.domain.executor.Executor;
import com.zygne.stockanalyzer.domain.executor.MainThread;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.*;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.base.*;
import com.zygne.stockanalyzer.domain.model.BarData;
import com.zygne.stockanalyzer.domain.model.DataSize;
import com.zygne.stockanalyzer.domain.model.Histogram;
import com.zygne.stockanalyzer.domain.model.Settings;
import com.zygne.stockanalyzer.domain.model.enums.TimeInterval;

import java.util.ArrayList;
import java.util.List;

public class DataFlow implements DataFetchInteractor.Callback,
        CacheWriteInteractor.Callback,
        CacheReadInteractor.Callback,
        CacheCheckerInteractor.Callback,
        MissingDataInteractor.Callback,
        DataMergeInteractor.Callback,
        HistogramInteractor.Callback,
        DateFilterInteractor.Callback{


    private final Callback callback;
    private final Executor executor;
    private final MainThread mainThread;
    private Logger logger;
    private String ticker;
    private TimeInterval timeInterval;
    private int timeFrame;
    private DataSize.Unit unit;
    private DataBroker dataBroker;
    private Settings settings;
    private final List<BarData> cachedData = new ArrayList<>();
    private final List<BarData> downloadedData = new ArrayList<>();
    private boolean useCache = false;


    public DataFlow(Executor executor, MainThread mainThread, Callback callback, Logger logger) {
        this.callback = callback;
        this.executor = executor;
        this.mainThread = mainThread;
        this.logger = logger;
    }

    public void fetchData(DataBroker dataBroker, Settings settings, String ticker, TimeInterval timeInterval, int timeFrame, DataSize.Unit unit, boolean cache) {
        this.dataBroker = dataBroker;
        this.settings = settings;
        this.ticker = ticker;
        this.timeInterval = timeInterval;
        this.timeFrame = timeFrame;
        this.unit = unit;
        this.useCache = cache;

        this.cachedData.clear();
        this.downloadedData.clear();

        if(timeInterval == TimeInterval.Day){
            cache = false;
        }
        if(timeInterval == TimeInterval.Week){
            cache = false;
        }
        if(timeInterval == TimeInterval.Month){
            cache = false;
        }

        if (cache) {
            new CacheCheckerInteractorImpl(executor, mainThread, this, settings.getCache(), ticker + "-" + timeInterval.name()).execute();
        } else {
            logger.log(Logger.LOG_LEVEL.INFO, "Download data for " + ticker.toUpperCase());
            new DataFetchInteractorImpl(executor, mainThread, this, ticker, timeInterval, new DataSize(timeFrame, unit), dataBroker).execute();
        }
    }

    @Override
    public void onCachedDataFound(String location) {
        logger.log(Logger.LOG_LEVEL.INFO, "Reading cache");
        new CacheReadInteractorImpl(executor, mainThread, this, location).execute();
    }

    @Override
    public void onCachedDataError() {
        new DataFetchInteractorImpl(executor, mainThread, this, ticker, timeInterval, new DataSize(timeFrame, unit), dataBroker).execute();
    }

    @Override
    public void onCachedDataRead(List<BarData> entries, long timeStamp) {
        cachedData.addAll(entries);
        long difference = System.currentTimeMillis() - timeStamp;

        long dayMs = 12 * 3600 * 1000;

        if (difference < dayMs) {
            new DateFilterInteractorImpl(executor, mainThread, this, entries, new DataSize(timeFrame, unit)).execute();
        } else {
            logger.log(Logger.LOG_LEVEL.INFO, "Checking for missing data");
            new MissingDataInteractorImpl(executor, mainThread, this, entries).execute();
        }
    }

    @Override
    public void onDataCached(List<BarData> lines) {
        new DateFilterInteractorImpl(executor, mainThread, this, lines, new DataSize(timeFrame, unit)).execute();
    }

    @Override
    public void onDataMerged(List<BarData> entries) {
        logger.log(Logger.LOG_LEVEL.INFO, "Updating cache with missing data");
        new CacheWriteInteractorImpl(executor, mainThread, this, settings.getCache(), ticker + "-" + timeInterval.name(), entries).execute();
    }

    @Override
    public void onDataFetched(List<BarData> entries, String timestamp) {
        downloadedData.addAll(entries);

        if (useCache) {
            logger.log(Logger.LOG_LEVEL.INFO, "Updating cache data");
            new DataMergeInteractorImpl(executor, mainThread, this, downloadedData, cachedData).execute();
        } else {
            logger.log(Logger.LOG_LEVEL.INFO, "Generating histogram");
            new HistogramInteractorImpl(executor, mainThread, this, downloadedData).execute();
        }
    }

    @Override
    public void onMissingDataCalculated(int daysMissing) {
        if (daysMissing > 0) {
            daysMissing = (int) Math.ceil(daysMissing / 30);
            new DataFetchInteractorImpl(executor, mainThread, this, ticker, timeInterval, new DataSize(daysMissing, DataSize.Unit.Month), dataBroker).execute();
        } else {
            new HistogramInteractorImpl(executor, mainThread, this, cachedData).execute();
        }
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
        callback.onDataFetched(data, dateRange);
    }

    @Override
    public void onDateFilterCompleted(List<BarData> lines) {
        new HistogramInteractorImpl(executor, mainThread, this, lines).execute();
    }

    public interface Callback {
        void onDataFetched(List<Histogram> data, String time);

        void onDataError();
    }
}

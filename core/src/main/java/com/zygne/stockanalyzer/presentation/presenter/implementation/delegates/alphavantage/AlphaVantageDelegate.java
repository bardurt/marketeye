package com.zygne.stockanalyzer.presentation.presenter.implementation.delegates.alphavantage;

import com.zygne.stockanalyzer.domain.executor.Executor;
import com.zygne.stockanalyzer.domain.executor.MainThread;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.*;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.av.AlphaVantageDataInteractor;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.av.AlphaVantageFundamentalsInteractor;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.av.AlphaVantageHistogramInteractorImpl;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.base.*;
import com.zygne.stockanalyzer.domain.model.*;
import com.zygne.stockanalyzer.domain.model.enums.TimeInterval;
import com.zygne.stockanalyzer.presentation.presenter.base.MainPresenter;
import com.zygne.stockanalyzer.presentation.presenter.implementation.delegates.alphavantage.flow.ResistanceFlow;

import java.util.ArrayList;
import java.util.List;

public class AlphaVantageDelegate implements MainPresenter{
//        DataFetchInteractor.Callback,
//        HistogramInteractor.Callback,
//        CacheWriteInteractor.Callback,
//        CacheReadInteractor.Callback,
//        CacheCheckerInteractor.Callback,
//        DataMergeInteractor.Callback,
//        MissingDataInteractor.Callback,
//        ResistanceFlow.Callback,
//        FundamentalsInteractor.Callback,
//        PriceGapInteractor.Callback,
//        AverageBarVolumeInteractor.Callback {

    private final View view;
    private List<Histogram> histogramList;
    private String ticker;
    private double percentile = 0;
    private boolean fundamentalData;
    private Fundamentals fundamentals;

    private TimeInterval timeInterval = TimeInterval.Five_Minutes;
    private int monthsToFetch = 24;
    private boolean downloadingData = false;
    private final Executor executor;
    private final MainThread mainThread;

    private final List<String> cachedData = new ArrayList<>();
    private final List<String> downloadedData = new ArrayList<>();

   // private final ResistanceFlow resistanceFlow;

    private final Settings settings;

    public AlphaVantageDelegate(Executor threadExecutor, MainThread mainThread, View view, Settings settings) {
        this.executor = threadExecutor;
        this.mainThread = mainThread;
        this.view = view;
        this.settings = settings;
       // this.resistanceFlow = new ResistanceFlow(executor, mainThread, this);
        List<TimeInterval> timeIntervals = new ArrayList<>();
        timeIntervals.add(TimeInterval.One_Minute);
        timeIntervals.add(TimeInterval.Five_Minutes);
        timeIntervals.add(TimeInterval.Fifteen_Minutes);
        timeIntervals.add(TimeInterval.Thirty_Minutes);
        timeIntervals.add(TimeInterval.Hour);
        view.onTimeFramesPrepared(timeIntervals, 1);

        List<DataLength> dataSize = new ArrayList<>();
        dataSize.add(new DataLength(1, "Month"));
        dataSize.add(new DataLength(3, "Months"));
        dataSize.add(new DataLength(6, "Months"));
        dataSize.add(new DataLength(12, "Months"));
        dataSize.add(new DataLength(24, "Months"));
        view.onDataSizePrepared(dataSize, dataSize.size() - 1);

        view.onConnected();
    }

    @Override
    public void getZones(String ticker, double percentile, TimeInterval timeInterval, int monthsToFetch, boolean fundamentalData) {
        if (settings == null) {
            notifySettingsError();
            return;
        }

        if (downloadingData) {
            return;
        }

        if (ticker.isEmpty()) {
            view.showError("No Symbol name!");
            return;
        }

        downloadedData.clear();
        cachedData.clear();

        this.percentile = percentile;
        this.timeInterval = timeInterval;
        this.fundamentalData = fundamentalData;
        this.monthsToFetch = monthsToFetch;

        downloadingData = true;
        this.ticker = ticker.replaceAll("\\s+", "");

        view.showError("");

        //new CacheCheckerInteractorImpl(executor, mainThread, this, settings.getCache(), ticker + "-" + timeInterval.name()).execute();
    }
//
//    @Override
//    public void onDataFetched(List<BarData> entries, String timestamp) {
//        downloadingData = false;
//
//        view.showLoading("Merging data");
//        new DataMergeInteractorImpl(executor, mainThread, this, downloadedData, cachedData).execute();
//    }
//
//    @Override
//    public void onDataFetchError(String message) {
//        downloadingData = false;
//        view.hideLoading();
//        view.showError(message);
//    }
//
//    @Override
//    public void onStatusUpdate(String message) {
//        view.onStatusUpdate(message);
//    }
//
//    @Override
//    public void onHistogramCreated(List<Histogram> data) {
//        this.histogramList = data;
//        resistanceFlow.start(histogramList, percentile);
//    }
//
//    @Override
//    public void onDataCached(List<String> lines) {
//        view.showLoading("Formatting data...");
//        new AlphaVantageHistogramInteractorImpl(executor, mainThread, this, lines).execute();
//    }
//
//    @Override
//    public void onCachedDataFound(String location) {
//        view.showLoading("Reading cached data...");
//        new CacheReadInteractorImpl(executor, mainThread, this, location).execute();
//    }
//
//    @Override
//    public void onCachedDataError() {
//        view.showLoading("Fetching data for " + ticker.toUpperCase() + " : " + timeInterval.toString() + " from Alpha Vantage");
//        new AlphaVantageDataInteractor(executor, mainThread, this, ticker, timeInterval, monthsToFetch, settings.getApiKey()).execute();
//    }
//
//    @Override
//    public void onCachedDataRead(List<String> entries, long timeStamp) {
//        cachedData.addAll(entries);
//        long difference = System.currentTimeMillis() - timeStamp;
//
//        long dayMs = 12 * 3600 * 1000;
//
//        if (difference < dayMs) {
//            new AlphaVantageHistogramInteractorImpl(executor, mainThread, this, entries).execute();
//        } else {
//            view.showLoading("Fetching latest data...");
//            new MissingDataInteractorImpl(executor, mainThread, this, entries).execute();
//        }
//    }

//    @Override
//    public void onDataMerged(List<String> entries) {
//        view.showLoading("Caching data...");
//        new CacheWriteInteractorImpl(executor, mainThread, this, settings.getCache(), ticker + "-" + timeInterval.name(), entries).execute();
//    }

//    @Override
//    public void onMissingDataCalculated(int daysMissing) {
//        if (daysMissing > 0) {
//            int months = Math.floorDiv(daysMissing, 30) + 1;
//            if (months > monthsToFetch) {
//                months = monthsToFetch;
//            }
//            new AlphaVantageDataInteractor(executor, mainThread, this, ticker, timeInterval, months, settings.getApiKey()).execute();
//        } else {
//            new AlphaVantageHistogramInteractorImpl(executor, mainThread, this, cachedData).execute();
//        }
//    }

//    @Override
//    public void onResistanceCompleted(List<LiquidityLevel> data) {
//        view.onResistanceFound(data);
//        new PriceGapInteractorImpl(executor, mainThread, this, histogramList).execute();
//    }

//    @Override
//    public void onPriceGapsFound(List<PriceGap> data) {
//        view.onPriceGapsFound(data);
//
//        if (fundamentalData) {
//            view.showLoading("Fetching fundamental data...");
//            new AlphaVantageFundamentalsInteractor(executor, mainThread, this, ticker, settings.getApiKey()).execute();
//        } else {
//            downloadingData = false;
//            view.hideLoading();
//            view.onComplete(ticker, timeInterval.toString());
//        }
//    }
//
//    @Override
//    public void onFundamentalsFetched(Fundamentals fundamentals) {
//        this.fundamentals = fundamentals;
//        view.hideLoading();
//        new AverageBarVolumeInteractorImpl(executor, mainThread, this, histogramList).execute();
//    }

    private void notifySettingsError() {
        view.hideLoading();
        view.showError("Cannot find API key in settings file");
    }

//    @Override
//    public void onAverageBarVolumeCalculated(long avgVol) {
//        fundamentals.setAvgVol(avgVol);
//        view.onFundamentalsLoaded(fundamentals);
//        downloadingData = false;
//        view.onComplete(ticker, timeInterval.toString());
//    }

    @Override
    public void toggleConnection() {

    }
}

package com.zygne.stockalyze.presentation.presenter.implementation.delegates.alphavantage;

import com.zygne.stockalyze.domain.executor.MainThread;
import com.zygne.stockalyze.domain.executor.Executor;
import com.zygne.stockalyze.domain.interactor.implementation.data.*;
import com.zygne.stockalyze.domain.interactor.implementation.data.base.*;
import com.zygne.stockalyze.domain.model.Fundamentals;
import com.zygne.stockalyze.domain.model.Histogram;
import com.zygne.stockalyze.domain.model.LiquidityZone;
import com.zygne.stockalyze.domain.model.Settings;
import com.zygne.stockalyze.domain.model.enums.TimeFrame;
import com.zygne.stockalyze.presentation.presenter.base.MainPresenter;
import com.zygne.stockalyze.presentation.presenter.implementation.delegates.alphavantage.flow.PivotFlow;
import com.zygne.stockalyze.presentation.presenter.implementation.delegates.alphavantage.flow.ResistanceFlow;
import com.zygne.stockalyze.presentation.presenter.implementation.delegates.alphavantage.flow.SupportFlow;

import java.util.ArrayList;
import java.util.List;

public class AlphaVantageDelegate implements MainPresenter,
        DataFetchInteractor.Callback,
        HistogramInteractor.Callback,
        CacheWriteInteractor.Callback,
        CacheReadInteractor.Callback,
        CacheCheckerInteractor.Callback,
        DataMergeInteractor.Callback,
        MissingDataInteractor.Callback,
        ResistanceFlow.Callback,
        SupportFlow.Callback,
        PivotFlow.Callback,
        FundamentalsInteractor.Callback {

    private final MainPresenter.View view;
    private List<Histogram> histogramList;
    private String ticker;
    private double percentile = 0;
    private boolean fundamentalData;

    private TimeFrame timeFrame = TimeFrame.Five_Minutes;
    private final int monthsToFetch = 24;
    private boolean downloadingData = false;
    private final Executor executor;
    private final MainThread mainThread;

    private final List<String> cachedData = new ArrayList<>();
    private final List<String> downloadedData = new ArrayList<>();

    private final ResistanceFlow resistanceFlow;
    private final SupportFlow supportFlow;
    private final PivotFlow pivotFlow;

    private final Settings settings;

    public AlphaVantageDelegate(Executor threadExecutor, MainThread mainThread, MainPresenter.View view, Settings settings) {
        this.executor = threadExecutor;
        this.mainThread = mainThread;
        this.view = view;
        this.settings = settings;
        this.resistanceFlow = new ResistanceFlow(executor, mainThread, this);
        this.supportFlow = new SupportFlow(executor, mainThread, this);
        this.pivotFlow = new PivotFlow(executor, mainThread, this);

        List<TimeFrame> timeFrames = new ArrayList<>();
        timeFrames.add(TimeFrame.One_Minute);
        timeFrames.add(TimeFrame.Five_Minutes);
        timeFrames.add(TimeFrame.Fifteen_Minutes);
        timeFrames.add(TimeFrame.Thirty_Minutes);
        timeFrames.add(TimeFrame.Hour);
        timeFrames.add(TimeFrame.Day);
        view.onTimeFramesPrepared(timeFrames, 1);
    }

    @Override
    public void getZones(String ticker, double percentile, TimeFrame timeFrame, boolean fundamentalData) {
        if(settings == null){
            notifySettingsError();
            return;
        }

        if (downloadingData) {
            return;
        }

        if (ticker.isEmpty()) {
            view.showError("No Ticker name!");
            return;
        }

        downloadedData.clear();
        cachedData.clear();

        this.percentile = percentile;
        this.timeFrame = timeFrame;
        this.fundamentalData = fundamentalData;

        downloadingData = true;
        this.ticker = ticker;

        view.showError("");

        new CacheCheckerInteractorImpl(executor, mainThread, this, settings.getCache(),ticker + "-" + timeFrame.name()).execute();
    }

    @Override
    public void onDataFetched(List<String> entries, String ticker, TimeFrame timeFrame) {
        downloadingData = false;
        downloadedData.addAll(entries);

        view.showLoading("Merging data");
        new DataMergeInteractorImpl(executor, mainThread, this, downloadedData, cachedData).execute();
    }

    @Override
    public void onDataFetchError(String message) {
        downloadingData = false;
        view.hideLoading();
        view.showError(message);
    }

    @Override
    public void onHistogramCreated(List<Histogram> data) {
        this.histogramList = data;
        resistanceFlow.start(histogramList, percentile);
    }

    @Override
    public void onDataCached(List<String> lines) {
        view.showLoading("Formatting data...");
        new HistogramInteractorImpl(executor, mainThread, this, lines).execute();
    }

    @Override
    public void onCachedDataFound(String location) {
        view.showLoading("Reading cached data...");
        System.out.println("Reading cached data...");
        new CacheReadInteractorImpl(executor, mainThread, this, location).execute();
    }

    @Override
    public void onCachedDataError() {
        view.showLoading("Fetching data for " + ticker.toUpperCase() + " from Alpha Vantage");
        new AlphaVantageDataInteractor(executor, mainThread, this, ticker, timeFrame, monthsToFetch, settings.getApiKey()).execute();
    }

    @Override
    public void onCachedDataRead(List<String> entries, long timeStamp) {
        cachedData.addAll(entries);
        long difference = System.currentTimeMillis() - timeStamp;

        long dayMs = 12 * 3600 * 1000;

        if (difference < dayMs) {
            new HistogramInteractorImpl(executor, mainThread, this, entries).execute();
        } else {
            new MissingDataInteractorImpl(executor, mainThread, this, entries).execute();
        }
    }

    @Override
    public void onDataMerged(List<String> entries) {
        view.showLoading("Caching data...");
        new CacheWriteInteractorImpl(executor, mainThread, this, settings.getCache(), ticker + "-" + timeFrame.name(), entries).execute();
    }

    @Override
    public void onMissingDataCalculated(int daysMissing) {
        if (daysMissing > 0) {
            int months = Math.floorDiv(daysMissing, 30) + 1;
            if (months > monthsToFetch) {
                months = monthsToFetch;
            }
            new AlphaVantageDataInteractor(executor, mainThread, this, ticker, timeFrame, months, settings.getApiKey()).execute();
        } else {
            new HistogramInteractorImpl(executor, mainThread, this, cachedData).execute();
        }
    }

    @Override
    public void onResistanceCompleted(List<LiquidityZone> data) {
        view.onResistanceFound(data);
        view.showLoading("Finding Support");
        pivotFlow.setResistance(data);
        supportFlow.start(histogramList, percentile);
    }

    @Override
    public void onSupportCompleted(List<LiquidityZone> data) {
        downloadingData = false;

        view.onSupportFound(data);

        view.showLoading("Finding Pivot");
        pivotFlow.setSupport(data);
        pivotFlow.findPivots();
    }

    @Override
    public void onPivotCompleted(List<LiquidityZone> pivots) {
        view.hideLoading();
        view.onPivotFound(pivots);

        if(fundamentalData) {
            new AlphaVantageFundamentalsInteractor(executor, mainThread, this, ticker, settings.getApiKey()).execute();
        } else {
            view.onComplete(ticker);
        }
    }

    @Override
    public void onFundamentalsFetched(Fundamentals fundamentals) {
        view.hideLoading();
        view.onFundamentalsLoaded(fundamentals);
        view.onComplete(ticker);
    }

    private void notifySettingsError(){
        view.hideLoading();
        view.showError("Cannot find API key in settings file");
    }
}

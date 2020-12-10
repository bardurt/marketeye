package com.zygne.stockanalyzer.presentation.presenter.implementation.delegates;

import com.zygne.stockanalyzer.domain.Api;
import com.zygne.stockanalyzer.domain.DataBroker;
import com.zygne.stockanalyzer.IbDataBroker;
import com.zygne.stockanalyzer.domain.executor.Executor;
import com.zygne.stockanalyzer.domain.executor.MainThread;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.*;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.av.AlphaVantageDataInteractor;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.av.AlphaVantageHistogramInteractorImpl;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.base.*;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.ib.IbDataInteractor;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.ib.IbHistogramInteractor;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.ib.IbMissingDataInteractor;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.ib.IbPartialDataInteractor;
import com.zygne.stockanalyzer.domain.model.*;
import com.zygne.stockanalyzer.domain.model.enums.TimeInterval;
import com.zygne.stockanalyzer.presentation.presenter.base.MainPresenter;
import com.zygne.stockanalyzer.presentation.presenter.implementation.delegates.alphavantage.flow.ResistanceFlow;

import java.util.ArrayList;
import java.util.List;

public class InteractiveBrokersDelegate implements MainPresenter,
        DataFetchInteractor.Callback,
        CacheWriteInteractor.Callback,
        CacheReadInteractor.Callback,
        CacheCheckerInteractor.Callback,
        MissingDataInteractor.Callback,
        DataMergeInteractor.Callback,
        HistogramInteractor.Callback,
        ResistanceFlow.Callback,
        FundamentalsInteractor.Callback,
        AverageBarVolumeInteractor.Callback,
        PriceGapInteractor.Callback,
        LiquiditySideInteractor.Callback,
        LiquiditySideFilterInteractor.Callback,
        LiquiditySidePriceInteractor.Callback,
        Api.ConnectionListener {

    private DataBroker dataBroker;

    private boolean connected = false;
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

    private final List<BarData> cachedData = new ArrayList<>();
    private final List<BarData> downloadedData = new ArrayList<>();

    private final ResistanceFlow resistanceFlow;

    private final Settings settings;

    public InteractiveBrokersDelegate(Executor threadExecutor, MainThread mainThread, View view, Settings settings) {
        this.executor = threadExecutor;
        this.mainThread = mainThread;
        this.dataBroker = new IbDataBroker();
        dataBroker.setConnectionListener(this);
        this.view = view;
        this.settings = settings;
        this.resistanceFlow = new ResistanceFlow(executor, mainThread, this);
        List<TimeInterval> timeIntervals = new ArrayList<>();
        timeIntervals.add(TimeInterval.One_Minute);
        timeIntervals.add(TimeInterval.Five_Minutes);
        timeIntervals.add(TimeInterval.Fifteen_Minutes);
        timeIntervals.add(TimeInterval.Thirty_Minutes);
        timeIntervals.add(TimeInterval.Hour);
        timeIntervals.add(TimeInterval.Day);
        timeIntervals.add(TimeInterval.Week);
        view.onTimeFramesPrepared(timeIntervals, 2);

        List<DataLength> dataSize = new ArrayList<>();
        dataSize.add(new DataLength(1, "Year"));
        dataSize.add(new DataLength(2, "Years"));
        dataSize.add(new DataLength(3, "Years"));
        dataSize.add(new DataLength(4, "Years"));
        dataSize.add(new DataLength(5, "Years"));
        view.onDataSizePrepared(dataSize, dataSize.size() - 1);
    }

    @Override
    public void getZones(String ticker, double percentile, TimeInterval timeInterval, int monthsToFetch, boolean fundamentalData) {
        if (!connected) {
            view.showError("Client not connected!");
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

        view.showLoading("Fetching data for " + ticker.toUpperCase() + "");

        new CacheCheckerInteractorImpl(executor, mainThread, this, settings.getCache(), ticker + "-" + timeInterval.name()).execute();

    }

    @Override
    public void onCachedDataFound(String location) {
        view.showLoading("Reading cached data...");
        new CacheReadInteractorImpl(executor, mainThread, this, location).execute();
    }

    @Override
    public void onCachedDataError() {
        new IbDataInteractor(executor, mainThread, this, ticker, timeInterval, monthsToFetch, dataBroker).execute();
    }

    @Override
    public void onCachedDataRead(List<BarData> entries, long timeStamp) {
        cachedData.addAll(entries);
        long difference = System.currentTimeMillis() - timeStamp;

        long dayMs = 12 * 3600 * 1000;

        if (difference < dayMs) {
            new IbHistogramInteractor(executor, mainThread, this, entries).execute();
        } else {
            new IbMissingDataInteractor(executor, mainThread, this, entries).execute();
        }
    }

    @Override
    public void onDataCached(List<BarData> lines) {
        new IbHistogramInteractor(executor, mainThread, this, lines).execute();
    }

    @Override
    public void onDataMerged(List<BarData> entries) {
        new CacheWriteInteractorImpl(executor, mainThread, this, settings.getCache(), ticker + "-" + timeInterval.name(), entries).execute();

    }

    @Override
    public void onDataFetched(List<BarData> entries, String timestamp) {
        downloadedData.addAll(entries);
        new DataMergeInteractorImpl(executor, mainThread, this, downloadedData, cachedData).execute();
    }

    @Override
    public void onMissingDataCalculated(int daysMissing) {
        System.out.println("Missing days " + daysMissing);
        if (daysMissing > 0) {
            view.showLoading("Fetching missing data");
            new IbPartialDataInteractor(executor, mainThread, this, ticker, timeInterval, daysMissing, dataBroker).execute();
        } else {
            new IbHistogramInteractor(executor, mainThread, this, cachedData).execute();
        }
    }

    @Override
    public void onDataFetchError(String message) {
        downloadingData = false;
    }

    @Override
    public void onStatusUpdate(String message) {
        view.onStatusUpdate(message);
    }

    @Override
    public void onHistogramCreated(List<Histogram> data) {
        this.histogramList = data;
        resistanceFlow.start(histogramList, percentile);
    }


    @Override
    public void onResistanceCompleted(List<LiquidityLevel> data) {
        view.onResistanceFound(data);

        downloadingData = false;
        view.hideLoading();

        new PriceGapInteractorImpl(executor, mainThread, this, histogramList).execute();
    }

    @Override
    public void onFundamentalsFetched(Fundamentals fundamentals) {
        this.fundamentals = fundamentals;
        view.hideLoading();
        new AverageBarVolumeInteractorImpl(executor, mainThread, this, histogramList).execute();
    }

    @Override
    public void onAverageBarVolumeCalculated(long avgVol) {
        fundamentals.setAvgVol(avgVol);
        view.onFundamentalsLoaded(fundamentals);
        downloadingData = false;
        view.onComplete(ticker, timeInterval.toString());
    }

    @Override
    public void onApiConnected() {
        connected = true;
        mainThread.post(() -> view.onConnected());
    }

    @Override
    public void onApiDisconnected() {
        connected = false;
        mainThread.post(() -> view.onDisconnected());
    }

    @Override
    public void toggleConnection() {
        if (!connected) {
            dataBroker.connect();
        } else {
            dataBroker.disconnect();
        }
    }

    @Override
    public void onPriceGapsFound(List<PriceGap> data) {
        view.hideLoading();
        view.onPriceGapsFound(data);
        view.onComplete(ticker, timeInterval.toString());
    }

    @Override
    public void onLiquiditySidesFiltered(List<LiquiditySide> data) {

    }

    @Override
    public void onLiquiditySidesCreated(List<LiquiditySide> data) {

    }

    @Override
    public void onLiquiditySideForPriceFound(List<LiquiditySide> data) {

    }
}

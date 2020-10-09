package com.zygne.stockalyze.presentation.presenter.implementation;

import com.zygne.stockalyze.domain.executor.Executor;
import com.zygne.stockalyze.domain.interactor.implementation.MarketTimeInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.data.*;
import com.zygne.stockalyze.domain.interactor.implementation.data.base.*;
import com.zygne.stockalyze.domain.model.*;
import com.zygne.stockalyze.domain.model.enums.MarketTime;
import com.zygne.stockalyze.domain.MainThread;
import com.zygne.stockalyze.presentation.presenter.base.BasePresenter;
import com.zygne.stockalyze.presentation.presenter.base.MainPresenter;

import java.util.ArrayList;
import java.util.List;

public class MainPresenterImpl extends BasePresenter implements MainPresenter,
        VolumePriceInteractor.Callback,
        VolumePriceGroupInteractor.Callback,
        LiquidityZoneInteractor.Callback,
        LiquidityZoneFilterInteractor.Callback,
        StatisticsInteractor.Callback,
        DataFetchInteractor.Callback,
        HistogramInteractor.Callback,
        StockFloatInteractor.Callback,
        MarketTimeInteractor.Callback,
        PowerZoneInteractor.Callback,
        PowerZoneFilterInteractor.Callback,
        PowerRatioInteractor.Callback,
        CacheWriteInteractor.Callback,
        CacheCheckerInteractor.Callback{

    private View view;
    private DataReport dataReport;
    private List<Histogram> histogramList;
    private String ticker;
    private double topFilter = 10;
    private int source = 0;
    private int vpaRule = 0;

    private boolean downloadingData = false;


    public MainPresenterImpl(Executor executor, MainThread mainThread, MainPresenter.View view) {
        super(executor, mainThread);
        this.view = view;
        this.mainThread = mainThread;
    }

    @Override
    public void filterZones(double topPercent) {
        if (dataReport.zones.isEmpty()) {
            view.onZonesFiltered(new ArrayList<>());
            return;
        }

        this.topFilter = topPercent;

        new LiquidityZoneFilterInteractorImpl(executor, mainThread, this, dataReport.zones, dataReport.statistics, topFilter).execute();
    }

    @Override
    public void getZones(String ticker, int source, int vpaRule) {
        if (downloadingData) {
            return;
        }

        this.source = source;
        downloadingData = true;
        dataReport = new DataReport();
        dataReport.ticker = ticker;
        this.ticker = ticker;

        view.showError("");

        new CacheCheckerInteractorImpl(executor, mainThread, this, ticker).execute();
    }

    @Override
    public void onStockFloatFetched(int stockFloat) {
        dataReport.stockFloat = stockFloat;
    }

    @Override
    public void onDataFetched(List<String> entries, String ticker) {
        downloadingData = false;
        dataReport.ticker = ticker;
        view.showLoading("Caching data...");
        new CacheWriteInteractorImpl(executor, mainThread, this, ticker, entries).execute();
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
        new PowerZoneInteractorImpl(executor, mainThread, this, histogramList).execute();
    }

    @Override
    public void onVolumePriceGroupCreated(List<VolumePriceGroup> data) {
        new LiquidityZoneInteractorImpl(executor, mainThread, this, data, dataReport.statistics).execute();
    }

    @Override
    public void onLiquidityZonesCreated(List<LiquidityZone> data) {
        dataReport.zones = data;
        new PowerRatioInteractorImpl(executor, mainThread, this, dataReport.powerZones, dataReport.zones).execute();
    }

    @Override
    public void onLiquidityZonesFiltered(List<LiquidityZone> data) {
        dataReport.filteredZones = data;

        view.hideLoading();
        view.onZonesFiltered(data);
    }

    @Override
    public void onStatisticsCalculated(Statistics statistics) {
        dataReport.statistics = statistics;
        new VolumePriceInteractorImpl(executor, mainThread, this, histogramList, vpaRule).execute();
    }

    @Override
    public void onVolumePriceCreated(List<VolumePriceLevel> data) {
        new VolumePriceGroupInteractorImpl(executor, mainThread, this, data).execute();
    }

    @Override
    public void onMarketTimeValidated(MarketTime marketTime) {
        dataReport.marketTime = marketTime;
    }

    @Override
    public void onPowerZonesCreated(List<PowerZone> data) {
        dataReport.powerZones = data;
        new PowerZoneFilterInteractorImpl(executor, mainThread, this, data, dataReport.openPrice).execute();
    }

    @Override
    public void onPowerZoneFiltered(List<PowerZone> data) {
        dataReport.filteredPowerZones = data;
        new StatisticsInteractorImpl(this, histogramList).execute();
    }

    @Override
    public void onPowerRatioCreated(List<LiquidityZone> data) {
        dataReport.filteredZones = data;
        view.hideLoading();
        view.onZonesCreated(data);

    }

    @Override
    public List<Histogram> getHistogram() {
        return histogramList;
    }

    @Override
    public void inDataCached(List<String> lines) {
        view.showLoading("Formatting data...");
        new HistogramInteractorImpl(executor, mainThread, this, lines).execute();
    }

    @Override
    public void onCachedDataFound(String location) {
        view.showLoading("Reading cached data...");
        new CsvReaderInteractor(executor, mainThread, this, location).execute();
    }

    @Override
    public void onCachedDataError() {
        if (source == 0) {
            view.showLoading("Fetching data for " + ticker.toUpperCase() + " from Yahoo Finance");
            new YahooDataInteractor(executor, mainThread, this, ticker).execute();
        } else {
            view.showLoading("Fetching data for " + ticker.toUpperCase() + " from Alpha Vantage");
            new AlphaVantageDataInteractor(executor, mainThread, this, ticker).execute();
        }
    }
}

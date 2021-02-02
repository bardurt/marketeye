package com.zygne.stockanalyzer.presentation.presenter.implementation.delegates;

import com.zygne.stockanalyzer.YahooDataBroker;
import com.zygne.stockanalyzer.domain.api.DataBroker;
import com.zygne.stockanalyzer.domain.executor.Executor;
import com.zygne.stockanalyzer.domain.executor.MainThread;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.*;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.base.*;
import com.zygne.stockanalyzer.domain.model.*;
import com.zygne.stockanalyzer.domain.model.enums.TimeInterval;
import com.zygne.stockanalyzer.presentation.presenter.base.MainPresenter;
import com.zygne.stockanalyzer.presentation.presenter.implementation.delegates.flow.DailyVolumeFlow;
import com.zygne.stockanalyzer.presentation.presenter.implementation.delegates.flow.SupplyFlow;

import java.util.ArrayList;
import java.util.List;

public class YahooFinanceDelegate implements MainPresenter,
        DataFetchInteractor.Callback,
        HistogramInteractor.Callback,
        SupplyFlow.Callback,
        FundamentalsInteractor.Callback,
        AverageBarVolumeInteractor.Callback,
        PriceGapInteractor.Callback,
        LiquiditySideInteractor.Callback,
        LiquiditySideFilterInteractor.Callback,
        LiquiditySidePriceInteractor.Callback,
        DailyVolumeFlow.Callback {

    private final DataBroker dataBroker;

    private final View view;
    private List<Histogram> histogramList;
    private String ticker;
    private double percentile = 0;
    private Fundamentals fundamentals;

    private TimeInterval timeInterval = TimeInterval.Five_Minutes;
    private boolean downloadingData = false;
    private final Executor executor;
    private final MainThread mainThread;

    private final List<BarData> cachedData = new ArrayList<>();
    private final List<BarData> downloadedData = new ArrayList<>();

    private final SupplyFlow supplyFlow;

    private String dateRange = "";

    public YahooFinanceDelegate(Executor threadExecutor, MainThread mainThread, View view, Settings settings) {
        this.executor = threadExecutor;
        this.mainThread = mainThread;
        this.dataBroker = new YahooDataBroker();
        this.view = view;
        this.supplyFlow = new SupplyFlow(executor, mainThread, this);
        List<TimeInterval> timeIntervals = new ArrayList<>();
        timeIntervals.add(TimeInterval.Day);
        timeIntervals.add(TimeInterval.Week);
        view.onTimeFramesPrepared(timeIntervals, 0);

        List<DataSize> dataSize = new ArrayList<>();
        dataSize.add(new DataSize(1, DataSize.Unit.Year));
        dataSize.add(new DataSize(2, DataSize.Unit.Year));
        dataSize.add(new DataSize(3, DataSize.Unit.Year));
        dataSize.add(new DataSize(4, DataSize.Unit.Year));
        dataSize.add(new DataSize(5, DataSize.Unit.Year));
        dataSize.add(new DataSize(10, DataSize.Unit.Year));
        view.onDataSizePrepared(dataSize, dataSize.size() - 2);

        view.toggleConnectionSettings(false);

        List<ViewComponent> viewComponents = new ArrayList<>();

        viewComponents.add(ViewComponent.VPA);
        viewComponents.add(ViewComponent.WICKS);
        viewComponents.add(ViewComponent.PRICE_GAPS);
        viewComponents.add(ViewComponent.SCRIPT);

        view.prepareView(viewComponents);
    }

    @Override
    public void getZones(String ticker, double percentile, TimeInterval timeInterval, int monthsToFetch, boolean fundamentalData) {

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

        downloadingData = true;
        this.ticker = ticker.replaceAll("\\s+", "");

        view.showError("");

        view.showLoading("Fetching data for " + ticker.toUpperCase() + "");

        new DataFetchInteractorImpl(executor, mainThread, this, ticker, timeInterval, new DataSize(monthsToFetch, DataSize.Unit.Year), dataBroker).execute();
    }

    @Override
    public void onDataFetched(List<BarData> entries, String timestamp) {
        new HistogramInteractorImpl(executor, mainThread, this, entries).execute();
    }


    @Override
    public void onDataFetchError(String message) {
        downloadingData = false;
        view.hideLoading();
        view.showError(message);
    }

    @Override
    public void onStatusUpdate(String message) {
        view.onStatusUpdate(message);
    }

    @Override
    public void onHistogramCreated(List<Histogram> data) {
        this.histogramList = data;
        dateRange = histogramList.get(histogramList.size() - 1).dateTime + " - " + histogramList.get(0).dateTime;
        supplyFlow.start(histogramList, percentile);
    }


    @Override
    public void onSupplyCompleted(List<LiquidityLevel> data) {
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
        view.onComplete(ticker, timeInterval.toString(), dateRange);
    }

    @Override
    public void toggleConnection() {
    }

    @Override
    public void findHighVolume() {

        new DailyVolumeFlow(executor, mainThread, this, dataBroker).findVolume(ticker);

    }

    @Override
    public void onPriceGapsFound(List<PriceGap> data) {
        view.hideLoading();
        view.onPriceGapsFound(data);
        view.onComplete(ticker, timeInterval.toString(), dateRange);
        findHighVolume();
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

    @Override
    public void onDailyHighVolumeFound(List<VolumeBarDetails> data, List<Histogram> histograms) {
        view.onHighVolumeBarFound(data);
    }
}

package com.zygne.stockanalyzer.presentation.presenter.implementation.delegates;

import com.zygne.stockanalyzer.AlphaVantageDataBroker;
import com.zygne.stockanalyzer.domain.Logger;
import com.zygne.stockanalyzer.domain.api.DataBroker;
import com.zygne.stockanalyzer.domain.executor.Executor;
import com.zygne.stockanalyzer.domain.executor.MainThread;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.base.HistogramInteractor;
import com.zygne.stockanalyzer.domain.model.*;
import com.zygne.stockanalyzer.domain.model.enums.TimeInterval;
import com.zygne.stockanalyzer.presentation.presenter.base.MainPresenter;
import com.zygne.stockanalyzer.presentation.presenter.implementation.flow.*;

import java.util.ArrayList;
import java.util.List;

public class AlphaVantageDelegate implements MainPresenter,
        HistogramInteractor.Callback,
        SupplyFlow.Callback,
        SupplyBinnedFlow.Callback,
        DailyVolumeFlow.Callback,
        DailySupplyFlow.Callback,
        DailyLiquidityFlow.Callback,
        PriceGapFlow.Callback,
        DataFlow.Callback,
        FundamentalsFlow.Callback {

    private final DataBroker dataBroker;

    private final View view;
    private List<Histogram> histogramList;
    private List<Histogram> dailyHistogramlist;
    private String ticker;
    private double percentile = 0;
    private boolean fundamentalData;
    private Fundamentals fundamentals;

    private TimeInterval timeInterval = TimeInterval.Five_Minutes;
    private boolean downloadingData = false;
    private final Executor executor;
    private final MainThread mainThread;

    private final DataFlow dataFlow;
    private final SupplyFlow supplyFlow;
    private final SupplyBinnedFlow supplyBinnedFlow;

    private final Settings settings;
    private DataSize dataSize;

    private Logger logger;

    private String dateRange = "";

    public AlphaVantageDelegate(Executor threadExecutor, MainThread mainThread, View view, Settings settings, Logger logger) {
        this.executor = threadExecutor;
        this.mainThread = mainThread;
        this.dataBroker = new AlphaVantageDataBroker(settings.getApiKey(), logger);
        this.logger = logger;
        this.view = view;
        this.view.showError("");
        this.settings = settings;
        this.supplyFlow = new SupplyFlow(executor, mainThread, this);
        this.supplyBinnedFlow = new SupplyBinnedFlow(executor, mainThread, this);
        this.dataFlow = new DataFlow(executor, mainThread, this, logger);

        List<TimeInterval> timeIntervals = new ArrayList<>();
        timeIntervals.add(TimeInterval.One_Minute);
        timeIntervals.add(TimeInterval.Five_Minutes);
        timeIntervals.add(TimeInterval.Fifteen_Minutes);
        timeIntervals.add(TimeInterval.Thirty_Minutes);
        timeIntervals.add(TimeInterval.Hour);
        timeIntervals.add(TimeInterval.Day);
        view.onTimeFramesPrepared(timeIntervals, 0);

        List<DataSize> dataSize = new ArrayList<>();
        dataSize.add(new DataSize(1, DataSize.Unit.Month));
        dataSize.add(new DataSize(3, DataSize.Unit.Month));
        dataSize.add(new DataSize(6, DataSize.Unit.Month));
        dataSize.add(new DataSize(12, DataSize.Unit.Month));
        dataSize.add(new DataSize(24, DataSize.Unit.Month));
        view.onDataSizePrepared(dataSize, dataSize.size() - 1);

        view.toggleConnectionSettings(false);

        List<ViewComponent> viewComponents = new ArrayList<>();

        viewComponents.add(ViewComponent.VPA);
        viewComponents.add(ViewComponent.WICKS);
        viewComponents.add(ViewComponent.PRICE_GAPS);
        viewComponents.add(ViewComponent.SCRIPT);

        view.prepareView(viewComponents);
    }

    @Override
    public void createReport(String ticker, double percentile, TimeInterval timeInterval, DataSize dataSize, boolean fundamentalData, boolean cache) {
        if (downloadingData) {
            return;
        }

        if (ticker.isEmpty()) {
            view.showError("No Symbol name!");
            return;
        }

        this.percentile = percentile;
        this.timeInterval = timeInterval;
        this.fundamentalData = fundamentalData;
        this.dataSize = dataSize;
        this.ticker = ticker;

        downloadingData = true;

        view.showError("");

        view.showLoading("Fetching data for " + ticker.toUpperCase() + "");

        dataFlow.fetchData(dataBroker, settings, ticker, timeInterval, dataSize.getSize(), dataSize.getUnit(), cache);
    }

    @Override
    public void onHistogramCreated(List<Histogram> data) {
        this.histogramList = data;
        view.onHistogramCreated(data);
        dateRange = histogramList.get(histogramList.size() - 1).dateTime + " - " + histogramList.get(0).dateTime;
        supplyFlow.start(histogramList, percentile, 0);
    }

    @Override
    public void onSupplyCompleted(List<LiquidityLevel> filtered, List<LiquidityLevel> raw) {
        view.onSupplyCreated(filtered, raw);
        supplyBinnedFlow.start(histogramList, percentile);
    }

    @Override
    public void toggleConnection() {
    }

    @Override
    public void findHighVolume() {
        new DailyVolumeFlow(executor, mainThread, this, dataBroker).findVolume(ticker, dataSize);
    }

    @Override
    public void onDailyHighVolumeFound(List<VolumeBarDetails> data, List<Histogram> histograms) {
        this.dailyHistogramlist = histograms;
        view.onDailyBarsCreated(histograms);
        new DailySupplyFlow(executor, mainThread, this, data, histogramList).start();
    }

    @Override
    public void onDailySupplyFound(List<VolumeBarDetails> data) {
        view.onHighVolumeBarFound(data);
        new DailyLiquidityFlow(executor, mainThread, this, dailyHistogramlist, logger).start();
    }

    @Override
    public void onDailyLiquidityFound(List<LiquiditySide> data) {
        view.onDailyLiquidityGenerated(data);
        logger.log(Logger.LOG_LEVEL.INFO, "Finding daily price gaps");
        new PriceGapFlow(executor, mainThread, this, dailyHistogramlist, PriceGapFlow.GapType.DAILY).start();
    }

    @Override
    public void onBinnedSupplyCompleted(List<LiquidityLevel> data) {
        downloadingData = false;
        view.hideLoading();
        view.onBinnedSupplyCreated(data);
        view.onComplete(ticker, timeInterval.toString(), dateRange);

        findHighVolume();
    }

    @Override
    public void onPriceGapsGenerated(List<PriceGap> data, PriceGapFlow.GapType gapType) {

        if(gapType == PriceGapFlow.GapType.DAILY) {
            view.onDailyPriceGapsFound(data);
            logger.log(Logger.LOG_LEVEL.INFO, "Daily price gaps completed");
            logger.log(Logger.LOG_LEVEL.INFO, "Finding intra day price gaps");
            new PriceGapFlow(executor, mainThread, this, histogramList, PriceGapFlow.GapType.INTRA_DAY).start();

        } else {
            logger.log(Logger.LOG_LEVEL.INFO, "Intra day price gaps completed");
            view.onIntraDayPriceGapsFound(data);
            logger.log(Logger.LOG_LEVEL.INFO, "Fetching fundamentals");
            FundamentalsFlow fundamentalsFlow = new FundamentalsFlow(executor, mainThread, this, settings);
            fundamentalsFlow.start(ticker, dailyHistogramlist);
        }
    }

    @Override
    public void onDataFetched(List<Histogram> data, String time) {
        this.histogramList = data;
        this.dateRange = time;
        view.onHistogramCreated(data);
        supplyFlow.start(histogramList, percentile, 0);
    }

    @Override
    public void onDataError() {
        view.hideLoading();
        view.showError("Unable to fetch data");
    }


    @Override
    public void onFundamentalsPrepared(Fundamentals fundamentals) {
        this.fundamentals = fundamentals;
        view.onFundamentalsLoaded(fundamentals);
        view.hideLoading();
    }

    @Override
    public void setAsset(DataBroker.Asset asset) {
        dataBroker.setAsset(asset);
    }
}

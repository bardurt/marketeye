package com.zygne.stockanalyzer.presentation.presenter.implementation.delegates;

import com.zygne.stockanalyzer.AlphaVantageDataBroker;
import com.zygne.stockanalyzer.CryptoCompareDataBroker;
import com.zygne.stockanalyzer.YahooDataBroker;
import com.zygne.stockanalyzer.domain.Logger;
import com.zygne.stockanalyzer.domain.api.DataBroker;
import com.zygne.stockanalyzer.domain.executor.Executor;
import com.zygne.stockanalyzer.domain.executor.MainThread;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.HistogramGroupingInteractorImpl;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.base.HistogramGroupingInteractor;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.base.HistogramInteractor;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.base.VolumePriceInteractor;
import com.zygne.stockanalyzer.domain.model.*;
import com.zygne.stockanalyzer.domain.model.enums.TimeInterval;
import com.zygne.stockanalyzer.presentation.presenter.base.MainPresenter;
import com.zygne.stockanalyzer.presentation.presenter.implementation.flow.*;

import java.util.ArrayList;
import java.util.List;

public class CryptoCompareDelegate implements MainPresenter,
        SupplyFlow.Callback,
        PriceGapFlow.Callback,
        DataFlow.Callback,
        HistogramGroupingInteractor.Callback {

    private final DataBroker dataBroker;

    private final View view;
    private List<Histogram> histogramListGrouped;
    private List<Histogram> histogramList;
    private String ticker;
    private double percentile = 0;

    private TimeInterval timeInterval = TimeInterval.Five_Minutes;
    private boolean downloadingData = false;
    private final Executor executor;
    private final MainThread mainThread;

    private final DataFlow dataFlow;
    private final SupplyFlow supplyFlow;

    private final Settings settings;
    private DataSize dataSize;

    private Logger logger;

    private String dateRange = "";

    public CryptoCompareDelegate(Executor threadExecutor, MainThread mainThread, View view, Settings settings, Logger logger) {
        this.executor = threadExecutor;
        this.mainThread = mainThread;
        this.dataBroker = new CryptoCompareDataBroker(settings.getApiKey(), logger);
        this.logger = logger;
        this.view = view;
        this.view.showError("");
        this.settings = settings;
        this.supplyFlow = new SupplyFlow(executor, mainThread, this);
        this.dataFlow = new DataFlow(executor, mainThread, this, logger);

        List<TimeInterval> timeIntervals = new ArrayList<>();
        timeIntervals.add(TimeInterval.Hour);
        timeIntervals.add(TimeInterval.Day);
        view.onTimeFramesPrepared(timeIntervals, 0);

        List<DataSize> dataSize = new ArrayList<>();
        dataSize.add(new DataSize(1, DataSize.Unit.Week));
        dataSize.add(new DataSize(2, DataSize.Unit.Week));
        dataSize.add(new DataSize(1, DataSize.Unit.Month));
        dataSize.add(new DataSize(3, DataSize.Unit.Month));
        dataSize.add(new DataSize(6, DataSize.Unit.Month));
        dataSize.add(new DataSize(1, DataSize.Unit.Year));
        dataSize.add(new DataSize(2, DataSize.Unit.Year));
        view.onDataSizePrepared(dataSize, dataSize.size() - 1);

        view.toggleConnectionSettings(false);

        List<ViewComponent> viewComponents = new ArrayList<>();

        viewComponents.add(ViewComponent.VPA);
        viewComponents.add(ViewComponent.WICKS);

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
        this.dataSize = dataSize;
        this.ticker = ticker;

        downloadingData = true;

        view.showError("");

        view.showLoading("Fetching data for " + ticker.toUpperCase() + "");

        dataFlow.fetchData(dataBroker, settings, ticker, TimeInterval.Hour, dataSize.getSize(), dataSize.getUnit(), cache);
    }

    @Override
    public void onSupplyCompleted(List<LiquidityLevel> filtered, List<LiquidityLevel> raw) {
        downloadingData = false;
        view.onHistogramCreated(histogramListGrouped);
        view.onSupplyCreated(filtered, raw);
        view.hideLoading();
        view.onComplete(ticker, timeInterval.toString(), dateRange);
    }

    @Override
    public void toggleConnection() {
    }

    @Override
    public void findHighVolume() {
    }

    @Override
    public void onPriceGapsGenerated(List<PriceGap> data, PriceGapFlow.GapType gapType) {
        logger.log(Logger.LOG_LEVEL.INFO, "Intra day price gaps completed");
        view.onIntraDayPriceGapsFound(data);
        view.hideLoading();
    }

    @Override
    public void onGapHistoryGenerated(GapHistory gapHistory) {

    }

    @Override
    public void onDataFetched(List<Histogram> data, String time) {
        this.histogramList = data;
        this.dateRange = time;

        if (timeInterval == TimeInterval.Day) {
            new HistogramGroupingInteractorImpl(executor, mainThread, this, data, HistogramGroupingInteractor.Group.DAY).execute();
        } else if (timeInterval == TimeInterval.Hour) {
            new HistogramGroupingInteractorImpl(executor, mainThread, this, data, HistogramGroupingInteractor.Group.HOUR).execute();

        }
    }

    @Override
    public void onDataError() {
        view.hideLoading();
        view.showError("Unable to fetch data");
        downloadingData = false;
    }

    @Override
    public void onLatestPriceFetched(double price) {
        downloadingData = false;
        view.hideLoading();

        view.onLatestPriceFetched(price);
    }

    @Override
    public void setAsset(DataBroker.Asset asset) {
        dataBroker.setAsset(asset);
    }

    @Override
    public void onHistogramGrouped(List<Histogram> data, HistogramGroupingInteractor.Group group) {
        this.histogramListGrouped = data;
        dateRange = histogramList.get(histogramList.size() - 1).dateTime + " - " + histogramList.get(0).dateTime;
        supplyFlow.start(histogramList, percentile, VolumePriceInteractor.PriceStructure.H);
    }
}
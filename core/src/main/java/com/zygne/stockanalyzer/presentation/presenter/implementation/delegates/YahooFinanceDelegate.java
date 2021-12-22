package com.zygne.stockanalyzer.presentation.presenter.implementation.delegates;

import com.zygne.stockanalyzer.YahooDataBroker;
import com.zygne.stockanalyzer.domain.Logger;
import com.zygne.stockanalyzer.domain.api.DataBroker;
import com.zygne.stockanalyzer.domain.executor.Executor;
import com.zygne.stockanalyzer.domain.executor.MainThread;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.base.VolumePriceInteractor;
import com.zygne.stockanalyzer.domain.model.*;
import com.zygne.stockanalyzer.domain.model.enums.TimeInterval;
import com.zygne.stockanalyzer.presentation.presenter.base.MainPresenter;
import com.zygne.stockanalyzer.presentation.presenter.implementation.flow.*;

import java.util.ArrayList;
import java.util.List;

public class YahooFinanceDelegate implements MainPresenter,
        SupplyFlow.Callback,
        DataFlow.Callback {

    private final DataBroker dataBroker;
    private final View view;
    private List<Histogram> histogramList;
    private String ticker;
    private double percentile = 0;
    private Fundamentals fundamentals;
    private DataSize dataSize;

    private TimeInterval timeInterval = TimeInterval.Five_Minutes;
    private boolean downloadingData = false;
    private final Executor executor;
    private final MainThread mainThread;

    private final DataFlow dataFlow;
    private final SupplyFlow supplyFlow;
    private Logger logger;

    private String dateRange = "";

    public YahooFinanceDelegate(Executor threadExecutor, MainThread mainThread, View view, Settings settings, Logger logger) {
        this.executor = threadExecutor;
        this.mainThread = mainThread;
        this.dataBroker = new YahooDataBroker(logger);
        this.logger = logger;
        this.view = view;
        this.view.showError("");
        this.supplyFlow = new SupplyFlow(executor, mainThread, this);
        this.dataFlow = new DataFlow(executor, mainThread, this, logger);

        List<TimeInterval> timeIntervals = new ArrayList<>();
        timeIntervals.add(TimeInterval.Day);
        view.onTimeFramesPrepared(timeIntervals, 0);

        List<DataSize> dataSize = new ArrayList<>();
        dataSize.add(new DataSize(1, DataSize.Unit.Month));
        dataSize.add(new DataSize(3, DataSize.Unit.Month));
        dataSize.add(new DataSize(6, DataSize.Unit.Month));
        dataSize.add(new DataSize(1, DataSize.Unit.Year));
        dataSize.add(new DataSize(2, DataSize.Unit.Year));
        dataSize.add(new DataSize(3, DataSize.Unit.Year));
        dataSize.add(new DataSize(5, DataSize.Unit.Year));
        dataSize.add(new DataSize(10, DataSize.Unit.Year));
        view.onDataSizePrepared(dataSize, dataSize.size() - 3);

        view.toggleConnectionSettings(false);

        List<ViewComponent> viewComponents = new ArrayList<>();

        viewComponents.add(ViewComponent.VPA);
        viewComponents.add(ViewComponent.WICKS);
        viewComponents.add(ViewComponent.PRICE_GAPS);
        viewComponents.add(ViewComponent.SCRIPT);
        viewComponents.add(ViewComponent.CHART);

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

        downloadingData = true;
        this.ticker = ticker.replaceAll("\\s+", "");

        view.showError("");

        view.showLoading("Fetching data for " + ticker.toUpperCase() + "");

        dataFlow.fetchData(dataBroker, null, ticker, timeInterval, dataSize.getSize(), dataSize.getUnit(), false);
    }


    @Override
    public void onSupplyCompleted(List<LiquidityLevel> filtered, List<LiquidityLevel> raw) {
        downloadingData = false;
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
    public void onDataFetched(List<Histogram> data, String time) {
        this.histogramList = data;
        this.dateRange = time;
        view.onHistogramCreated(data);
        supplyFlow.start(histogramList, percentile, VolumePriceInteractor.PriceStructure.OHLCM);
    }

    @Override
    public void onLatestPriceFetched(double price) {
        view.hideLoading();
        view.onLatestPriceFetched(price);
    }

    @Override
    public void onDataError() {
        view.hideLoading();
        view.showError("Unable to fetch data");
        downloadingData = false;
    }

    @Override
    public void setAsset(DataBroker.Asset asset) {
        dataBroker.setAsset(asset);
    }
}

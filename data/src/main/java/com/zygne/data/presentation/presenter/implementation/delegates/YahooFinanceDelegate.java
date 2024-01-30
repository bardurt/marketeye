package com.zygne.data.presentation.presenter.implementation.delegates;

import com.zygne.data.YahooDataBroker;
import com.zygne.data.domain.DataBroker;
import com.zygne.data.domain.interactor.implementation.data.base.VolumePriceInteractor;
import com.zygne.data.domain.model.*;
import com.zygne.data.domain.model.enums.TimeInterval;
import com.zygne.data.presentation.presenter.base.MainPresenter;
import com.zygne.data.presentation.presenter.implementation.flow.*;
import com.zygne.arch.domain.Logger;
import com.zygne.arch.domain.executor.Executor;
import com.zygne.arch.domain.executor.MainThread;

import java.util.ArrayList;
import java.util.List;

public class YahooFinanceDelegate implements MainPresenter,
        SupplyFlow.Callback,
        DataFlow.Callback {

    private final DataBroker dataBroker;
    private final View view;
    private String ticker;
    private double percentile = 0;

    private TimeInterval timeInterval = TimeInterval.Day;
    private boolean downloadingData = false;

    private final DataFlow dataFlow;
    private final SupplyFlow supplyFlow;

    private String dateRange = "";

    public YahooFinanceDelegate(Executor threadExecutor, MainThread mainThread, View view, Logger logger) {
        this.dataBroker = new YahooDataBroker(logger);
        this.view = view;
        this.view.showError("");
        this.supplyFlow = new SupplyFlow(threadExecutor, mainThread, this);
        this.dataFlow = new DataFlow(threadExecutor, mainThread, this, logger);

        List<TimeInterval> timeIntervals = new ArrayList<>();
        timeIntervals.add(TimeInterval.Day);
        view.onTimeFramesPrepared(timeIntervals, 0);

        List<DataSize> dataSize = new ArrayList<>();
        dataSize.add(new DataSize(1, DataSize.Unit.Year));
        dataSize.add(new DataSize(2, DataSize.Unit.Year));
        dataSize.add(new DataSize(3, DataSize.Unit.Year));
        dataSize.add(new DataSize(5, DataSize.Unit.Year));
        dataSize.add(new DataSize(10, DataSize.Unit.Year));
        view.onDataSizePrepared(dataSize, dataSize.size() - 3);

        view.prepareView();
    }

    @Override
    public void createReport(String ticker, double percentile, TimeInterval timeInterval, DataSize dataSize) {

        if (downloadingData) {
            return;
        }

        if (ticker.isEmpty()) {
            view.showError("No Symbol name!");
            return;
        }

        this.percentile = percentile;
        this.timeInterval = timeInterval;

        downloadingData = true;
        this.ticker = ticker.replaceAll("\\s+", "");

        view.showError("");

        view.showLoading("Fetching data for " + ticker.toUpperCase() + "");

        dataFlow.fetchData(dataBroker, ticker, timeInterval, dataSize.getSize(), dataSize.getUnit(), false);
    }


    @Override
    public void onSupplyCompleted(List<LiquidityLevel> filtered, List<LiquidityLevel> raw) {
        downloadingData = false;
        view.onSupplyCreated(filtered, raw);
        view.hideLoading();
        view.onComplete(ticker, timeInterval.toString(), dateRange);
    }

    @Override
    public void onDataFetched(List<Histogram> data, String time) {
        this.dateRange = time;
        view.onHistogramCreated(data);
        supplyFlow.start(data, percentile, VolumePriceInteractor.PriceStructure.OHLCM);
    }

    @Override
    public void onDataError() {
        view.hideLoading();
        view.showError("Unable to fetch data");
        downloadingData = false;
    }
}

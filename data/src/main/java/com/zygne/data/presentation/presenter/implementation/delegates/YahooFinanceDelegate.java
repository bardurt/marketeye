package com.zygne.data.presentation.presenter.implementation.delegates;

import com.zygne.data.YahooDataBroker;
import com.zygne.data.domain.DataBroker;
import com.zygne.data.domain.interactor.implementation.data.base.VolumePriceInteractor;
import com.zygne.data.domain.model.*;
import com.zygne.data.presentation.presenter.base.MainPresenter;
import com.zygne.data.presentation.presenter.implementation.flow.*;
import com.zygne.arch.domain.Logger;
import com.zygne.arch.domain.executor.Executor;
import com.zygne.arch.domain.executor.MainThread;

import java.util.List;

public class YahooFinanceDelegate implements MainPresenter,
        SupplyFlow.Callback,
        DataFlow.Callback {

    private final DataBroker dataBroker;
    private final View view;
    private String ticker;
    private double percentile = 0;
    private List<Histogram> histogramList;

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

        view.prepareView();
    }

    @Override
    public void createReport(String ticker) {

        if (downloadingData) {
            return;
        }

        if (ticker.isEmpty()) {
            view.showError("No Symbol name!");
            return;
        }

        downloadingData = true;
        this.ticker = ticker.replaceAll("\\s+", "");

        view.showError("");

        view.showLoading("Fetching data for " + ticker.toUpperCase() + "");

        dataFlow.fetchData(dataBroker, ticker, 12);
    }


    @Override
    public void onSupplyCompleted(List<LiquidityLevel> filtered, List<LiquidityLevel> raw) {
        downloadingData = false;
        view.onHistogramCreated(histogramList);
        view.onSupplyCreated(filtered, raw);
        view.hideLoading();
        view.onComplete(ticker, dateRange);
    }

    @Override
    public void onDataFetched(List<Histogram> data, String time) {
        this.dateRange = time;
        this.histogramList = data;

        supplyFlow.start(data, percentile, VolumePriceInteractor.PriceStructure.OHLCM);
    }

    @Override
    public void onDataError() {
        view.hideLoading();
        view.showError("Unable to fetch data");
        downloadingData = false;
    }
}

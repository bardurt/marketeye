package com.zygne.data.presentation.presenter.implementation;

import com.zygne.data.YahooDataBroker;
import com.zygne.data.domain.DataBroker;
import com.zygne.data.domain.model.Histogram;
import com.zygne.data.presentation.presenter.base.MainPresenter;
import com.zygne.arch.domain.Logger;
import com.zygne.arch.domain.executor.Executor;
import com.zygne.arch.domain.executor.MainThread;
import com.zygne.arch.presentation.presenter.base.BasePresenter;
import com.zygne.data.presentation.presenter.implementation.flow.DataFlow;

import java.util.List;

public class MainPresenterImpl extends BasePresenter implements MainPresenter,
        DataFlow.Callback {

    private final DataBroker dataBroker;
    private final View view;
    private String ticker;
    private List<Histogram> histogramList;
    private boolean downloadingData = false;
    private final DataFlow dataFlow;
    private String dateRange = "";

    public MainPresenterImpl(Executor executor, MainThread mainThread, MainPresenter.View view, Logger logger) {
        super(executor, mainThread);
        this.mainThread = mainThread;
        this.dataBroker = new YahooDataBroker(logger);
        this.view = view;
        this.view.showError("");
        this.dataFlow = new DataFlow(executor, mainThread, this, logger);

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

        dataFlow.fetchData(dataBroker, ticker, 5);
    }

    @Override
    public void onDataFetched(List<Histogram> data, String time) {
        this.downloadingData = false;
        this.dateRange = time;
        this.histogramList = data;
        view.hideLoading();
        view.onComplete(histogramList, ticker, dateRange);
    }

    @Override
    public void onDataError() {
        view.hideLoading();
        view.showError("Unable to fetch data");
        downloadingData = false;
    }
}

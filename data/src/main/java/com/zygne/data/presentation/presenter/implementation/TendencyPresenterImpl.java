package com.zygne.data.presentation.presenter.implementation;

import com.zygne.data.Assets;
import com.zygne.data.YahooDataBroker;
import com.zygne.data.domain.DataBroker;
import com.zygne.data.domain.interactor.implementation.data.RatioInteractorImpl;
import com.zygne.data.domain.interactor.implementation.data.TendencyInteractorImpl;
import com.zygne.data.domain.interactor.implementation.data.base.TendencyInteractor;
import com.zygne.data.domain.model.*;
import com.zygne.data.presentation.presenter.base.TendencyPresenter;
import com.zygne.data.presentation.presenter.implementation.flow.DataFlow;
import com.zygne.arch.domain.Logger;
import com.zygne.arch.domain.executor.Executor;
import com.zygne.arch.domain.executor.MainThread;
import com.zygne.arch.presentation.presenter.base.BasePresenter;

import java.util.ArrayList;
import java.util.List;

public class TendencyPresenterImpl extends BasePresenter implements
        TendencyPresenter,
        DataFlow.Callback,
        TendencyInteractor.Callback {

    private final View view;
    private final DataFlow dataFlow;
    private final Logger logger;
    private final DataBroker dataBroker;
    private List<Histogram> histograms;

    public TendencyPresenterImpl(Executor executor, MainThread mainThread, View view, Logger logger) {
        super(executor, mainThread);
        this.view = view;
        this.logger = logger;
        this.dataBroker = new YahooDataBroker(logger);
        this.dataFlow = new DataFlow(executor, mainThread, this, logger);
        setUp();
    }

    private void setUp() {
        List<Asset> assets = new ArrayList<>(Assets.assetList);
        view.onTendencyAssetsPrepared(assets, 0);
    }

    @Override
    public void createTendency(String symbol) {
        dataFlow.fetchData(dataBroker, symbol, 25, "");
    }

    @Override
    public void onDataFetched(List<Histogram> data, String time) {
        logger.log(Logger.LOG_LEVEL.INFO, "Creating Tendency");
        this.histograms = data;
        new TendencyInteractorImpl(executor, mainThread, this, data).execute();
    }

    @Override
    public void onDataError() {

    }

    @Override
    public void onTendencyReportCreated(TendencyReport tendencyReport) {
        logger.log(Logger.LOG_LEVEL.INFO, "Tendency Created");
        view.onTendencyReportCreated(histograms, tendencyReport);
    }
}

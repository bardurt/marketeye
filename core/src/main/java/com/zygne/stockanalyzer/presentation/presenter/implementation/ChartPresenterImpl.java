package com.zygne.stockanalyzer.presentation.presenter.implementation;

import com.zygne.stockanalyzer.YahooDataBroker;
import com.zygne.stockanalyzer.domain.Logger;
import com.zygne.stockanalyzer.domain.api.DataBroker;
import com.zygne.stockanalyzer.domain.executor.Executor;
import com.zygne.stockanalyzer.domain.executor.MainThread;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.PriceImbalanceInteractorImpl;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.base.PriceImbalanceInteractor;
import com.zygne.stockanalyzer.domain.model.*;
import com.zygne.stockanalyzer.domain.model.enums.TimeInterval;
import com.zygne.stockanalyzer.presentation.presenter.base.BasePresenter;
import com.zygne.stockanalyzer.presentation.presenter.base.ChartPresenter;
import com.zygne.stockanalyzer.presentation.presenter.implementation.flow.DataFlow;
import com.zygne.stockanalyzer.presentation.presenter.implementation.flow.PriceGapFlow;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ChartPresenterImpl extends BasePresenter implements ChartPresenter, DataFlow.Callback,
        PriceGapFlow.Callback,
        PriceImbalanceInteractor.Callback {

    private View view;

    private List<Histogram> histogramList = new ArrayList<>();
    private List<PriceGap> priceGapList;
    private List<LiquidityLevel> liquidityLevels;
    private boolean dataReady = false;

    public ChartPresenterImpl(Executor executor, MainThread mainThread, View view, Logger logger) {
        super(executor, mainThread);
        this.view = view;
    }

    @Override
    public void setHistograms(List<Histogram> histograms) {
        this.histogramList.clear();

        for (Histogram histogram : histograms) {
            histogramList.add(histogram.deepCopy());
        }

        dataReady = true;
    }

    @Override
    public void setAsset(DataBroker.Asset asset) {
    }

    @Override
    public void getChartData(String ticker, TimeInterval timeInterval, DataSize dataSize) {

        if (dataReady) {
            onDataFetched(histogramList, "");
        }
    }

    @Override
    public void onDataFetched(List<Histogram> data, String time) {
        histogramList = data;
        new PriceGapFlow(executor, mainThread, this, data, PriceGapFlow.GapType.DAILY).start();
    }

    @Override
    public void onLatestPriceFetched(double price) {

    }

    @Override
    public void onDataError() {

    }

    @Override
    public void onPriceGapsGenerated(List<PriceGap> data, PriceGapFlow.GapType gapType) {
        this.priceGapList = data;
        new PriceImbalanceInteractorImpl(executor, mainThread, this, histogramList).execute();
    }

    @Override
    public void onGapHistoryGenerated(GapHistory gapHistory) {

    }

    @Override
    public void onPriceImbalanceCompleted(List<PriceImbalance> data) {
        view.onChartReady(histogramList, priceGapList, data, liquidityLevels);
        dataReady = false;
    }

    @Override
    public void setSupply(List<LiquidityLevel> liquidityLevels) {
        this.liquidityLevels = liquidityLevels;
    }
}

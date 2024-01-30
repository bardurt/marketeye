package com.zygne.data.presentation.presenter.implementation;

import com.zygne.data.domain.interactor.implementation.data.PriceImbalanceInteractorImpl;
import com.zygne.data.domain.interactor.implementation.data.base.PriceImbalanceInteractor;
import com.zygne.data.domain.model.*;
import com.zygne.data.domain.model.enums.TimeInterval;
import com.zygne.data.presentation.presenter.base.ChartPresenter;
import com.zygne.data.presentation.presenter.implementation.flow.DataFlow;
import com.zygne.data.presentation.presenter.implementation.flow.PriceGapFlow;
import com.zygne.arch.domain.Logger;
import com.zygne.arch.domain.executor.Executor;
import com.zygne.arch.domain.executor.MainThread;
import com.zygne.arch.presentation.presenter.base.BasePresenter;

import java.util.ArrayList;
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

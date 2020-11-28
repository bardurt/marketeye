package com.zygne.stockanalyzer.presentation.presenter.implementation.delegates.alphavantage.flow;

import com.zygne.stockanalyzer.domain.executor.Executor;
import com.zygne.stockanalyzer.domain.executor.MainThread;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.*;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.base.*;
import com.zygne.stockanalyzer.domain.model.*;

import java.util.List;

public class SupportFlow implements VolumePriceInteractor.Callback,
        VolumePriceSumInteractor.Callback,
        LiquidityLevelInteractor.Callback,
        LiquidityLevelFilterInteractor.Callback,
        FundamentalsInteractor.Callback,
        BreakPointInteractor.Callback {

    private final Executor executor;
    private final MainThread mainThread;
    private final Callback callback;
    private List<Histogram> histogramList;
    private double percentile;

    public SupportFlow(Executor executor, MainThread mainThread, Callback callback) {
        this.executor = executor;
        this.mainThread = mainThread;
        this.callback = callback;
    }

    public void start(List<Histogram> histogramList, double percentile) {
        this.histogramList = histogramList;
        this.percentile = percentile;
        new VolumePriceInteractorImpl(executor, mainThread, this, histogramList, 1).execute();

    }

    @Override
    public void onBreakPointsCalculated(List<LiquidityLevel> data) {
        callback.onSupportCompleted(data);
    }

    @Override
    public void onFundamentalsFetched(Fundamentals fundamentals) { }

    @Override
    public void onLiquidityLevelsFiltered(List<LiquidityLevel> data) {
        new BullishBreakPointInteractorImpl(executor, mainThread, this, histogramList, data).execute();
    }

    @Override
    public void onLiquidityLevelsCreated(List<LiquidityLevel> data) {
        new LiquidityLevelFilterInteractorImpl(executor, mainThread, this, data, percentile).execute();
    }

    @Override
    public void onVolumePriceSumCreated(List<VolumePriceSum> data) {
        new LiquidityLevelInteractorImpl(executor, mainThread, this, data).execute();
    }

    @Override
    public void onVolumePriceCreated(List<VolumePrice> data) {
        new VolumePriceSumInteractorImpl(executor, mainThread, this, data).execute();
    }

    public interface Callback {
        void onSupportCompleted(List<LiquidityLevel> data);
    }
}


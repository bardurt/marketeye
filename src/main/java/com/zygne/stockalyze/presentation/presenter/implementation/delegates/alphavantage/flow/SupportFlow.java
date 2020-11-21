package com.zygne.stockalyze.presentation.presenter.implementation.delegates.alphavantage.flow;

import com.zygne.stockalyze.domain.executor.MainThread;
import com.zygne.stockalyze.domain.executor.Executor;
import com.zygne.stockalyze.domain.interactor.implementation.data.*;
import com.zygne.stockalyze.domain.interactor.implementation.data.base.*;
import com.zygne.stockalyze.domain.model.*;

import java.util.List;

public class SupportFlow implements VolumePriceInteractor.Callback,
        VolumePriceGroupInteractor.Callback,
        LiquidityZoneInteractor.Callback,
        LiquidityZoneFilterInteractor.Callback,
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
    public void onBreakPointsCalculated(List<LiquidityZone> data) {
        callback.onSupportCompleted(data);
    }

    @Override
    public void onFundamentalsFetched(Fundamentals fundamentals) { }

    @Override
    public void onLiquidityZonesFiltered(List<LiquidityZone> data) {
        new BreakPointInteractorImpl(executor, mainThread, this, histogramList, data).execute();
    }

    @Override
    public void onLiquidityZonesCreated(List<LiquidityZone> data) {
        new LiquidityZoneFilterInteractorImpl(executor, mainThread, this, data, percentile).execute();
    }

    @Override
    public void onVolumePriceGroupCreated(List<VolumePriceGroup> data) {
        new LiquidityZoneInteractorImpl(executor, mainThread, this, data).execute();
    }

    @Override
    public void onVolumePriceCreated(List<VolumePrice> data) {
        new VolumePriceGroupInteractorImpl(executor, mainThread, this, data).execute();
    }

    public interface Callback {
        void onSupportCompleted(List<LiquidityZone> data);
    }
}


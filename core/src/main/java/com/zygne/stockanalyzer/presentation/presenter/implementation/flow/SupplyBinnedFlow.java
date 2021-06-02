package com.zygne.stockanalyzer.presentation.presenter.implementation.flow;

import com.zygne.stockanalyzer.domain.executor.Executor;
import com.zygne.stockanalyzer.domain.executor.MainThread;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.*;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.base.*;
import com.zygne.stockanalyzer.domain.model.*;

import java.util.List;

public class SupplyBinnedFlow implements VolumePriceInteractor.Callback,
        VolumePriceSumInteractor.Callback,
        LiquidityLevelInteractor.Callback,
        LiquidityLevelFilterInteractor.Callback {

    private final Executor executor;
    private final MainThread mainThread;
    private final Callback callback;
    private List<Histogram> histogramList;
    private double percentile;

    public SupplyBinnedFlow(Executor executor, MainThread mainThread, Callback callback) {
        this.executor = executor;
        this.mainThread = mainThread;
        this.callback = callback;
    }

    public void start(List<Histogram> histogramList, double percentile) {
        this.histogramList = histogramList;
        this.percentile = percentile;
        new VolumePriceBinInteractorImpl(executor, mainThread, this, histogramList, 0).execute();
    }

    @Override
    public void onLiquidityLevelsFiltered(List<LiquidityLevel> data) {
        callback.onBinnedSupplyCompleted(data);
    }

    @Override
    public void onLiquidityLevelsCreated(List<LiquidityLevel> data) {
        callback.onBinnedSupplyCompleted(data);
        //new LiquidityLevelFilterInteractorImpl(executor, mainThread, this, data, percentile).execute();
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
        void onBinnedSupplyCompleted(List<LiquidityLevel> data);
    }
}

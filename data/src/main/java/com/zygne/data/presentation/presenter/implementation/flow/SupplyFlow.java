package com.zygne.data.presentation.presenter.implementation.flow;

import com.zygne.data.domain.interactor.implementation.data.*;
import com.zygne.data.domain.interactor.implementation.data.base.*;
import com.zygne.data.domain.model.*;
import com.zygne.arch.domain.executor.Executor;
import com.zygne.arch.domain.executor.MainThread;

import java.util.List;

public class SupplyFlow implements VolumePriceInteractor.Callback,
        VolumePriceSumInteractor.Callback,
        LiquidityLevelInteractor.Callback,
        LiquidityLevelFilterInteractor.Callback {

    private final Executor executor;
    private final MainThread mainThread;
    private final Callback callback;
    private List<Histogram> histogramList;
    private double percentile;
    private List<LiquidityLevel> rawLevels;

    public SupplyFlow(Executor executor, MainThread mainThread, Callback callback) {
        this.executor = executor;
        this.mainThread = mainThread;
        this.callback = callback;
    }

    public void start(List<Histogram> histogramList, double percentile, VolumePriceInteractor.PriceStructure priceStructure) {
        this.histogramList = histogramList;
        this.percentile = percentile;
        new VolumePriceInteractorImpl(executor, mainThread, this, histogramList, priceStructure).execute();
    }

    @Override
    public void onLiquidityLevelsFiltered(List<LiquidityLevel> data) {
        callback.onSupplyCompleted(data, rawLevels);
    }

    @Override
    public void onLiquidityLevelsCreated(List<LiquidityLevel> data) {
        rawLevels = data;
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
        void onSupplyCompleted(List<LiquidityLevel> filtered, List<LiquidityLevel> raw);
    }
}

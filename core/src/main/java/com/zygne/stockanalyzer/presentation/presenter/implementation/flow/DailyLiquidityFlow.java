package com.zygne.stockanalyzer.presentation.presenter.implementation.flow;

import com.zygne.stockanalyzer.domain.Logger;
import com.zygne.stockanalyzer.domain.executor.Executor;
import com.zygne.stockanalyzer.domain.executor.MainThread;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.LiquiditySideFilterInteractorImpl;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.LiquiditySideInteractorImpl;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.base.LiquiditySideFilterInteractor;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.base.LiquiditySideInteractor;
import com.zygne.stockanalyzer.domain.model.Histogram;
import com.zygne.stockanalyzer.domain.model.LiquiditySide;

import java.util.List;

public class DailyLiquidityFlow implements
        LiquiditySideInteractor.Callback,
        LiquiditySideFilterInteractor.Callback{

    private static final double PERCENTILE = 99;

    private final Executor executor;
    private final MainThread mainThread;
    private final Callback callback;
    private List<Histogram> histogramList;
    private Logger logger;

    public DailyLiquidityFlow(Executor executor, MainThread mainThread, Callback callback, List<Histogram> histogramList, Logger logger) {
        this.executor = executor;
        this.mainThread = mainThread;
        this.callback = callback;
        this.histogramList = histogramList;
        this.logger = logger;
    }

    public void start() {
        logger.log(Logger.LOG_LEVEL.INFO, "Finding daily liquidity");
        new LiquiditySideInteractorImpl(executor, mainThread, this, histogramList, 2).execute();
    }

    @Override
    public void onLiquiditySidesCreated(List<LiquiditySide> data) {
        logger.log(Logger.LOG_LEVEL.INFO, "Filtering daily liquidity");
        new LiquiditySideFilterInteractorImpl(executor, mainThread, this, data, PERCENTILE).execute();
    }

    @Override
    public void onLiquiditySidesFiltered(List<LiquiditySide> data) {
        callback.onDailyLiquidityFound(data);
    }

    public interface Callback {
        void onDailyLiquidityFound(List<LiquiditySide> data);
    }
}

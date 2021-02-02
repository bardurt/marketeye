package com.zygne.stockanalyzer.presentation.presenter.implementation.delegates.flow;

import com.zygne.stockanalyzer.domain.executor.Executor;
import com.zygne.stockanalyzer.domain.executor.MainThread;
import com.zygne.stockanalyzer.domain.model.Histogram;

import java.util.List;

public class LiquidityFlow {

    private final Executor executor;
    private final MainThread mainThread;
    private final Callback callback;
    private List<Histogram> histogramList;

    public LiquidityFlow(Executor executor, MainThread mainThread, Callback callback, List<Histogram> histogramList) {
        this.executor = executor;
        this.mainThread = mainThread;
        this.callback = callback;
        this.histogramList = histogramList;
    }

    public void start(){}

    public interface Callback {

    }
}

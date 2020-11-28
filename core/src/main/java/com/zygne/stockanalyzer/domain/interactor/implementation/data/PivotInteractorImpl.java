package com.zygne.stockanalyzer.domain.interactor.implementation.data;

import com.zygne.stockanalyzer.domain.executor.Executor;
import com.zygne.stockanalyzer.domain.executor.MainThread;
import com.zygne.stockanalyzer.domain.interactor.base.BaseInteractor;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.base.PivotInteractor;
import com.zygne.stockanalyzer.domain.model.LiquidityLevel;

import java.util.ArrayList;
import java.util.List;

public class PivotInteractorImpl extends BaseInteractor implements PivotInteractor {

    private final Callback callback;
    private final List<LiquidityLevel> resistance;
    private final List<LiquidityLevel> support;

    public PivotInteractorImpl(Executor executor, MainThread mainThread, Callback callback, List<LiquidityLevel> resistance, List<LiquidityLevel> support) {
        super(executor, mainThread);
        this.callback = callback;
        this.resistance = resistance;
        this.support = support;
    }

    @Override
    public void run() {

        List<LiquidityLevel> pivots = new ArrayList<>();

        for(LiquidityLevel r : resistance){
            for(LiquidityLevel s : support){
                if(r.price == s.price){
                    pivots.add(r);
                }
            }
        }

        mainThread.post(() -> callback.onPivotsFound(pivots));
    }
}

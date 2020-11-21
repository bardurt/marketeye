package com.zygne.stockalyze.domain.interactor.implementation.data;

import com.zygne.stockalyze.domain.executor.MainThread;
import com.zygne.stockalyze.domain.executor.Executor;
import com.zygne.stockalyze.domain.interactor.base.BaseInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.data.base.PivotInteractor;
import com.zygne.stockalyze.domain.model.LiquidityZone;

import java.util.ArrayList;
import java.util.List;

public class PivotInteractorImpl extends BaseInteractor implements PivotInteractor {

    private final Callback callback;
    private final List<LiquidityZone> resistance;
    private final List<LiquidityZone> support;

    public PivotInteractorImpl(Executor executor, MainThread mainThread, Callback callback, List<LiquidityZone> resistance, List<LiquidityZone> support) {
        super(executor, mainThread);
        this.callback = callback;
        this.resistance = resistance;
        this.support = support;
    }

    @Override
    public void run() {

        List<LiquidityZone> pivots = new ArrayList<>();

        for(LiquidityZone r : resistance){
            for(LiquidityZone s : support){
                if(r.price == s.price){
                    pivots.add(r);
                }
            }
        }

        mainThread.post(() -> callback.onPivotsFound(pivots));
    }
}

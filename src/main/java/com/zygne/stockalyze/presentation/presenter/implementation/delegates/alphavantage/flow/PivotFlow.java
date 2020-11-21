package com.zygne.stockalyze.presentation.presenter.implementation.delegates.alphavantage.flow;

import com.zygne.stockalyze.domain.executor.MainThread;
import com.zygne.stockalyze.domain.executor.Executor;
import com.zygne.stockalyze.domain.interactor.implementation.data.PivotInteractorImpl;
import com.zygne.stockalyze.domain.interactor.implementation.data.base.PivotInteractor;
import com.zygne.stockalyze.domain.model.LiquidityZone;

import java.util.List;

public class PivotFlow implements PivotInteractor.Callback {

    private final Executor executor;
    private final MainThread mainThread;
    private final Callback callback;

    private List<LiquidityZone> resistance;
    private List<LiquidityZone> support;

    public PivotFlow(Executor executor, MainThread mainThread, Callback callback) {
        this.executor = executor;
        this.mainThread = mainThread;
        this.callback = callback;
    }

    public void setResistance(List<LiquidityZone> resistance) {
        this.resistance = resistance;
    }

    public void setSupport(List<LiquidityZone> support) {
        this.support = support;
    }

    public void findPivots(){
        if(resistance != null && support != null) {
            new PivotInteractorImpl(executor, mainThread, this, resistance, support).execute();
        }
    }

    @Override
    public void onPivotsFound(List<LiquidityZone> data) {
        callback.onPivotCompleted(data);
    }

    public interface Callback{
        void onPivotCompleted(List<LiquidityZone> pivots);
    }
}

package com.zygne.stockalyze.domain.interactor.implementation.data;

import com.zygne.stockalyze.domain.executor.MainThread;
import com.zygne.stockalyze.domain.executor.Executor;
import com.zygne.stockalyze.domain.interactor.base.BaseInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.data.base.BreakPointInteractor;
import com.zygne.stockalyze.domain.model.Histogram;
import com.zygne.stockalyze.domain.model.LiquidityLevel;

import java.util.List;

public class BreakPointInteractorImpl extends BaseInteractor implements BreakPointInteractor {

    private final Callback callback;
    private final List<Histogram> histogramList;
    private final List<LiquidityLevel> liquidityLevelList;

    public BreakPointInteractorImpl(Executor executor, MainThread mainThread, Callback callback, List<Histogram> histogramList, List<LiquidityLevel> liquidityLevelList) {
        super(executor, mainThread);
        this.callback = callback;
        this.histogramList = histogramList;
        this.liquidityLevelList = liquidityLevelList;
    }

    @Override
    public void run() {

        for(LiquidityLevel e : liquidityLevelList){

            double volumeSum = 0;
            int hitCount = 0;

            for(Histogram h : histogramList) {
                if(h.getDirection() == Histogram.Direction.Up){
                    if(h.inBody(e.price)){
                        volumeSum += h.volume;
                        hitCount++;
                    }
                }
            }

            e.setBreakPoint(volumeSum / hitCount);
        }

        mainThread.post(() -> callback.onBreakPointsCalculated(liquidityLevelList));
    }
}

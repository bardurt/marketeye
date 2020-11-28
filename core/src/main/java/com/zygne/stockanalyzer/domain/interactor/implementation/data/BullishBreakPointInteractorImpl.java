package com.zygne.stockanalyzer.domain.interactor.implementation.data;

import com.zygne.stockanalyzer.domain.executor.Executor;
import com.zygne.stockanalyzer.domain.executor.MainThread;
import com.zygne.stockanalyzer.domain.interactor.base.BaseInteractor;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.base.BreakPointInteractor;
import com.zygne.stockanalyzer.domain.model.Histogram;
import com.zygne.stockanalyzer.domain.model.LiquidityLevel;

import java.util.List;

public class BullishBreakPointInteractorImpl extends BaseInteractor implements BreakPointInteractor {

    private final Callback callback;
    private final List<Histogram> histogramList;
    private final List<LiquidityLevel> liquidityLevelList;

    public BullishBreakPointInteractorImpl(Executor executor, MainThread mainThread, Callback callback, List<Histogram> histogramList, List<LiquidityLevel> liquidityLevelList) {
        super(executor, mainThread);
        this.callback = callback;
        this.histogramList = histogramList;
        this.liquidityLevelList = liquidityLevelList;
    }

    @Override
    public void run() {

        for(LiquidityLevel e : liquidityLevelList){

            double highest = 0;

            for(Histogram h : histogramList) {
                if(h.getDirection() == Histogram.Direction.Up){
                    if(h.inBody(e.price)){

                        if(h.volume > highest){
                            highest = h.volume;
                        }
                    }
                }
            }

            e.setBreakPoint(highest);
        }

        mainThread.post(() -> callback.onBreakPointsCalculated(liquidityLevelList));
    }
}

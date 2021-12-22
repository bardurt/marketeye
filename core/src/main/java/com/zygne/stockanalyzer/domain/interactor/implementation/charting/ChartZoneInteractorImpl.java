package com.zygne.stockanalyzer.domain.interactor.implementation.charting;

import com.zygne.stockanalyzer.domain.executor.Executor;
import com.zygne.stockanalyzer.domain.executor.MainThread;
import com.zygne.stockanalyzer.domain.interactor.base.BaseInteractor;
import com.zygne.stockanalyzer.domain.model.LiquiditySide;
import com.zygne.stockanalyzer.domain.model.graphics.ChartLine;
import com.zygne.stockanalyzer.domain.model.graphics.ChartObject;
import com.zygne.stockanalyzer.domain.model.graphics.ChartZone;

import java.util.ArrayList;
import java.util.List;

public class ChartZoneInteractorImpl extends BaseInteractor implements ChartZoneInteractor {

    private final Callback callback;
    private final List<LiquiditySide> data;

    public ChartZoneInteractorImpl(Executor executor, MainThread mainThread, Callback callback, List<LiquiditySide> data) {
        super(executor, mainThread);
        this.callback = callback;
        this.data = data;
    }

    @Override
    public void run() {

        List<ChartObject> zones = new ArrayList<>();

        for(LiquiditySide liquiditySide : data){
            ChartLine line = new ChartLine();

            if(liquiditySide.type == LiquiditySide.SELL){
                line.color = ChartObject.Color.RED;
            } else {
                line.color = ChartObject.Color.GREEN;
            }


            line.level = liquiditySide.start;
            line.size = 1;
            zones.add(line);
        }


        callback.onChartZoneCreated(zones);

    }
}

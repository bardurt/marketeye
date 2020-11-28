package com.zygne.stockanalyzer.domain.interactor.implementation.charting;

import com.zygne.stockanalyzer.domain.model.LiquiditySide;
import com.zygne.stockanalyzer.domain.model.graphics.ChartObject;
import com.zygne.stockanalyzer.domain.model.graphics.ChartZone;

import java.util.ArrayList;
import java.util.List;

public class ChartZoneInteractorImpl implements ChartZoneInteractor {

    private final Callback callback;
    private final List<LiquiditySide> data;

    public ChartZoneInteractorImpl(Callback callback, List<LiquiditySide> data) {
        this.callback = callback;
        this.data = data;
    }

    @Override
    public void execute() {

        List<ChartObject> zones = new ArrayList<>();

        for(LiquiditySide liquiditySide : data){
            ChartZone zone = new ChartZone();

            if(liquiditySide.type == LiquiditySide.RECJECT){
                zone.color = ChartObject.Color.RED;
            } else {
                zone.color = ChartObject.Color.GREEN;
            }

            zone.top = liquiditySide.end;
            zone.bottom = liquiditySide.start;
            zone.transparency = 95;

            zones.add(zone);
        }


        callback.onChartZoneCreated(zones);

    }
}

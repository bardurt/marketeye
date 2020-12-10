package com.zygne.stockanalyzer.domain.interactor.implementation.charting;

import com.zygne.stockanalyzer.domain.executor.Executor;
import com.zygne.stockanalyzer.domain.executor.MainThread;
import com.zygne.stockanalyzer.domain.interactor.base.BaseInteractor;
import com.zygne.stockanalyzer.domain.model.PriceGap;
import com.zygne.stockanalyzer.domain.model.graphics.ChartObject;
import com.zygne.stockanalyzer.domain.model.graphics.ChartZone;

import java.util.ArrayList;
import java.util.List;

public class PriceGapZoneInteractor extends BaseInteractor implements ChartZoneInteractor {

    private final Callback callback;
    private final List<PriceGap> data;


    public PriceGapZoneInteractor(Executor executor, MainThread mainThread, Callback callback, List<PriceGap> data) {
        super(executor, mainThread);
        this.callback = callback;
        this.data = data;
    }

    @Override
    public void run() {

        List<ChartObject> zones = new ArrayList<>();

        for (PriceGap p : data) {
            ChartZone zone2 = new ChartZone();


            zone2.color = ChartObject.Color.BLUE;
            zone2.top = p.getInnerEnd();
            zone2.bottom = p.getInnerStart();
            zone2.transparency = 90;

            zones.add(zone2);
        }


        mainThread.post(() -> callback.onChartZoneCreated(zones));
    }

}
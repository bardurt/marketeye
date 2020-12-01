package com.zygne.stockanalyzer.domain.interactor.implementation.data;

import com.zygne.stockanalyzer.domain.executor.Executor;
import com.zygne.stockanalyzer.domain.executor.MainThread;
import com.zygne.stockanalyzer.domain.interactor.base.BaseInteractor;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.base.LiquiditySideInteractor;
import com.zygne.stockanalyzer.domain.model.CandleStick;
import com.zygne.stockanalyzer.domain.model.Histogram;
import com.zygne.stockanalyzer.domain.model.LiquiditySide;

import java.util.ArrayList;
import java.util.List;

public class LiquiditySideInteractorImpl extends BaseInteractor implements LiquiditySideInteractor {

    private static final double MIN_CHANGE = 3.0;

    private final Callback callback;
    private final List<Histogram> data;
    private final double minSize;

    public LiquiditySideInteractorImpl(Executor executor, MainThread mainThread, Callback callback, List<Histogram> data, double minSize) {
        super(executor, mainThread);
        this.callback = callback;
        this.data = data;
        this.minSize = minSize / 100;
    }


    @Override
    public void run() {
        List<CandleStick> sticks = new ArrayList<>();

        for (Histogram e : data) {
            sticks.add(new CandleStick(e));
        }

        List<LiquiditySide> zones = new ArrayList<>();


        for (CandleStick e : sticks) {
            double change = ((e.top - e.bottom) / e.bottom) * 100;

            if (change > MIN_CHANGE) {

                if (e.upperWick / e.getSize() >= minSize) {
                    LiquiditySide p = new LiquiditySide();
                    p.start = e.bodyTop;
                    p.end = e.top;
                    p.type = LiquiditySide.REJECT;
                    p.timeStamp = e.timeStamp;
                    p.volume = e.volume;

                    if(e.bullish){
                        p.strength = LiquiditySide.WEAK;
                    } else {
                        p.strength = LiquiditySide.STRONG;
                    }

                    zones.add(p);
                }

                if (e.lowerWick / e.getSize() >= minSize) {
                    LiquiditySide p = new LiquiditySide();
                    p.start = e.bottom;
                    p.end = e.bodyBottom;
                    p.type = LiquiditySide.ACCEPT;
                    p.timeStamp = e.timeStamp;
                    p.volume = e.volume;

                    if(e.bullish){
                        p.strength = LiquiditySide.STRONG;
                    } else {
                        p.strength = LiquiditySide.WEAK;
                    }

                    zones.add(p);
                }
            }
        }

        zones.sort(new LiquiditySide.VolumeComparator());

        int size = zones.size();

        for (int i = 0; i < zones.size(); i++) {
            zones.get(i).volumeRank = size - i;
            zones.get(i).percentile = ((i + 1) / (double) size) * 100;
        }

        mainThread.post(() -> callback.onLiquiditySidesCreated(zones));

    }
}

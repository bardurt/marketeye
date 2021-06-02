package com.zygne.stockanalyzer.domain.interactor.implementation.data;

import com.zygne.stockanalyzer.domain.executor.Executor;
import com.zygne.stockanalyzer.domain.executor.MainThread;
import com.zygne.stockanalyzer.domain.interactor.base.BaseInteractor;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.base.LiquiditySideInteractor;
import com.zygne.stockanalyzer.domain.model.CandleStick;
import com.zygne.stockanalyzer.domain.model.Histogram;
import com.zygne.stockanalyzer.domain.model.LiquiditySide;
import com.zygne.stockanalyzer.domain.model.PriceGap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LiquiditySideInteractorImpl extends BaseInteractor implements LiquiditySideInteractor {

    private static final double MIN_CHANGE = 2.0;

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
        Collections.sort(data, new Histogram.TimeComparator());
        List<LiquiditySide> zones = findSides();

        zones = findBroken(zones);

        List<LiquiditySide> validSides = new ArrayList<>();

        for (LiquiditySide e : zones) {
            if (!e.broken) {
                validSides.add(e);
            }
        }

        validSides.sort(new LiquiditySide.VolumeComparator());

        int size = validSides.size();

        for (int i = 0; i < validSides.size(); i++) {
            validSides.get(i).volumeRank = size - i;
            validSides.get(i).percentile = ((i + 1) / (double) size) * 100;
        }

        mainThread.post(() -> callback.onLiquiditySidesCreated(validSides));

    }

    private List<LiquiditySide> findSides(){

        List<CandleStick> sticks = new ArrayList<>();

        for (Histogram e : data) {
            sticks.add(new CandleStick(e));
        }

        List<LiquiditySide> zones = new ArrayList<>();


        for (int i = 0; i < data.size(); i++) {

            CandleStick e = sticks.get(i);

            double change = ((e.top - e.bottom) / e.bottom) * 100;

            if (change > MIN_CHANGE) {

                if (e.upperWick / e.getSize() >= minSize) {
                    LiquiditySide p = new LiquiditySide();
                    p.start = e.bodyTop;
                    p.end = e.top;
                    p.type = LiquiditySide.REJECT;
                    p.timeStamp = e.timeStamp;
                    p.volume = e.volume;

                    p.index = i;

                    zones.add(p);
                }

                if (e.lowerWick / e.getSize() >= minSize) {
                    LiquiditySide p = new LiquiditySide();
                    p.start = e.bottom;
                    p.end = e.bodyBottom;
                    p.type = LiquiditySide.ACCEPT;
                    p.timeStamp = e.timeStamp;
                    p.volume = e.volume;

                    p.index = i;
                    zones.add(p);
                }

            }
        }

        return zones;
    }

    private List<LiquiditySide> findBroken(List<LiquiditySide> sides){

        for(LiquiditySide e : sides){
            for(int i = e.index+1; i < data.size(); i++){
                Histogram h = data.get(i);

                if(h.intersects(e.getEnd())){
                     e.broken = true;
                     break;
                }
            }

        }

        return sides;
    }
}

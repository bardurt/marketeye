package com.zygne.stockalyze.domain.interactor.implementation.data;

import com.zygne.stockalyze.domain.executor.MainThread;
import com.zygne.stockalyze.domain.executor.Executor;
import com.zygne.stockalyze.domain.interactor.base.BaseInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.data.base.LiquiditySideInteractor;
import com.zygne.stockalyze.domain.model.CandleStick;
import com.zygne.stockalyze.domain.model.Histogram;
import com.zygne.stockalyze.domain.model.LiquiditySide;

import java.util.ArrayList;
import java.util.List;

public class LiquiditySideInteractorImpl extends BaseInteractor implements LiquiditySideInteractor {

    private static final double MIN_SIZE = 0.05;

    private final Callback callback;
    private final List<Histogram> data;

    public LiquiditySideInteractorImpl(Executor executor, MainThread mainThread, Callback callback, List<Histogram> data) {
        super(executor, mainThread);
        this.callback = callback;
        this.data = data;
    }


    @Override
    public void run() {
        List<CandleStick> sticks = new ArrayList<>();

        for (Histogram e : data) {
            sticks.add(new CandleStick(e));
        }

        List<LiquiditySide> zones = new ArrayList<>();

        for (CandleStick e : sticks) {

            if (e.upperWick > e.bodySize / 4) {
                LiquiditySide p = new LiquiditySide();
                p.start = e.bodyTop;
                p.end = e.top;
                p.type = LiquiditySide.RECJECT;
                p.timeStamp = e.timeStamp;
                p.volume = e.volume;
                zones.add(p);
            }

            if (e.lowerWick > e.bodySize / 4) {
                LiquiditySide p = new LiquiditySide();
                p.start = e.bottom;
                p.end = e.bodyBottom;
                p.type = LiquiditySide.ACCEPT;
                p.timeStamp = e.timeStamp;
                p.volume = e.volume;
                zones.add(p);
            }
        }

        zones.sort(new LiquiditySide.VolumeComparator());

        int size = zones.size();

        for (int i = 0; i < zones.size(); i++) {
            zones.get(i).volumeRank = size - i;
            zones.get(i).perecentile = ((i + 1) / (double) size) * 100;
        }

        mainThread.post(() -> callback.onLiquiditySidesCreated(zones));

    }
}

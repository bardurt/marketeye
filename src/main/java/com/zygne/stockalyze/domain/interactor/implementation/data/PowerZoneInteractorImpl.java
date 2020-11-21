package com.zygne.stockalyze.domain.interactor.implementation.data;

import com.zygne.stockalyze.domain.executor.MainThread;
import com.zygne.stockalyze.domain.executor.Executor;
import com.zygne.stockalyze.domain.interactor.base.BaseInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.data.base.PowerZoneInteractor;
import com.zygne.stockalyze.domain.model.CandleStick;
import com.zygne.stockalyze.domain.model.Histogram;
import com.zygne.stockalyze.domain.model.PowerZone;

import java.util.ArrayList;
import java.util.List;

public class PowerZoneInteractorImpl extends BaseInteractor implements PowerZoneInteractor {

    private static final double MIN_SIZE = 0.05;

    private final Callback callback;
    private final List<Histogram> data;

    public PowerZoneInteractorImpl(Executor executor, MainThread mainThread, Callback callback, List<Histogram> data) {
        super(executor, mainThread);
        this.callback = callback;
        this.data = data;
    }


    @Override
    public void run() {

        System.out.println("PowerZoneInteractorImpl");
        List<CandleStick> sticks = new ArrayList<>();

        for (Histogram e : data) {
            sticks.add(new CandleStick(e));
        }

        List<PowerZone> zones = new ArrayList<>();

        for (CandleStick e : sticks) {

            if (e.top > e.bodyTop) {
                PowerZone p = new PowerZone();
                p.start = e.bodyTop;
                p.end = e.top;
                p.type = PowerZone.RECJECT;
                p.timeStamp = e.timeStamp;
                p.volume = e.volume;
                zones.add(p);
            }

            if (e.bottom < e.bodyBottom) {
                PowerZone p = new PowerZone();
                p.start = e.bottom;
                p.end = e.bodyBottom;
                p.type = PowerZone.ACCEPT;
                p.timeStamp = e.timeStamp;
                p.volume = e.volume;
                zones.add(p);
            }
        }

        zones.sort(new PowerZone.VolumeComparator());

        int size = zones.size();

        for (int i = 0; i < zones.size(); i++) {
            zones.get(i).volumeRank = i + 1;
            zones.get(i).perecentile = ((i + 1) / (double) size) * 100;
        }

        mainThread.post(() -> callback.onPowerZonesCreated(zones));

    }
}

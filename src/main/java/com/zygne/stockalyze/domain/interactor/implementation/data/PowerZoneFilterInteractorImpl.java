package com.zygne.stockalyze.domain.interactor.implementation.data;

import com.zygne.stockalyze.domain.executor.MainThread;
import com.zygne.stockalyze.domain.executor.Executor;
import com.zygne.stockalyze.domain.interactor.base.BaseInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.data.base.PowerZoneFilterInteractor;
import com.zygne.stockalyze.domain.model.PowerZone;

import java.util.ArrayList;
import java.util.List;

public class PowerZoneFilterInteractorImpl extends BaseInteractor implements PowerZoneFilterInteractor {

    private final Callback callback;
    private final List<PowerZone> data;
    private final double percentile;

    public PowerZoneFilterInteractorImpl(Executor executor, MainThread mainThread, Callback callback, List<PowerZone> data, double percentile) {
        super(executor, mainThread);
        this.callback = callback;
        this.data = data;
        this.percentile = percentile;
    }

    @Override
    public void run() {

        List<PowerZone> filtered = new ArrayList<>();

        for (int i = 1; i < data.size(); i++) {
            PowerZone e = data.get(i);

            if (e.perecentile > percentile) {
                filtered.add(e);
            }
        }

        mainThread.post(() -> callback.onPowerZonesFiltered(filtered));


    }
}

package com.zygne.stockalyze.domain.interactor.implementation.data;

import com.zygne.stockalyze.domain.executor.MainThread;
import com.zygne.stockalyze.domain.executor.Executor;
import com.zygne.stockalyze.domain.interactor.base.BaseInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.data.base.LiquidityZoneInteractor;
import com.zygne.stockalyze.domain.model.LiquidityZone;
import com.zygne.stockalyze.domain.model.VolumePriceGroup;

import java.util.ArrayList;
import java.util.List;

public class LiquidityZoneInteractorImpl extends BaseInteractor implements LiquidityZoneInteractor {

    private final Callback callback;
    private final List<VolumePriceGroup> data;

    public LiquidityZoneInteractorImpl(Executor executor, MainThread mainThread, Callback callback, List<VolumePriceGroup> data) {
        super(executor, mainThread);
        this.callback = callback;
        this.data = data;
    }

    @Override
    public void run() {
        System.out.println("LiquidityZoneInteractorImpl");
        List<LiquidityZone> formatted = new ArrayList<>();

        for (VolumePriceGroup e : data) {
            LiquidityZone s = new LiquidityZone(e.price, e.totalSize, e.orderCount);
            formatted.add(s);
        }

        formatted.sort(new LiquidityZone.VolumeComparator());

        int size = formatted.size();

        for (int i = 0; i < formatted.size(); i++) {
            formatted.get(i).rank = i + 1;
            formatted.get(i).percentile = ((i + 1) / (double) size) * 100;
        }

        mainThread.post(() -> callback.onLiquidityZonesCreated(formatted));

    }
}

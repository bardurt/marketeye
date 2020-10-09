package com.zygne.stockalyze.domain.interactor.implementation.data;

import com.zygne.stockalyze.domain.MainThread;
import com.zygne.stockalyze.domain.executor.Executor;
import com.zygne.stockalyze.domain.interactor.base.BaseInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.data.base.LiquidityZoneInteractor;
import com.zygne.stockalyze.domain.model.LiquidityZone;
import com.zygne.stockalyze.domain.model.Statistics;
import com.zygne.stockalyze.domain.model.VolumePriceGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LiquidityZoneInteractorImpl extends BaseInteractor implements LiquidityZoneInteractor {

    private final Callback callback;
    private final List<VolumePriceGroup> data;
    private final Statistics statistics;

    public LiquidityZoneInteractorImpl(Executor executor, MainThread mainThread, Callback callback, List<VolumePriceGroup> data, Statistics statistics) {
        super(executor, mainThread);
        this.callback = callback;
        this.data = data;
        this.statistics = statistics;
    }

    @Override
    public void run() {

        List<LiquidityZone> formatted = new ArrayList<>();

        for (VolumePriceGroup e : data) {
            LiquidityZone s = new LiquidityZone(e.price, e.totalSize, e.orderCount);
            s.relativeVolume = e.totalSize / statistics.mean;
            s.volumePercentage = (e.totalSize / (double) statistics.cumulativeVolume) * 100;
            formatted.add(s);
        }

        formatted.sort(new LiquidityZone.VolumeComparator());
        Collections.reverse(formatted);

        int size = formatted.size();

        for (int i = 0; i < formatted.size(); i++) {
            formatted.get(i).rank = i + 1;
            formatted.get(i).percentile = ((i + 1) / (double) size) * 100;
        }

        mainThread.post(new Runnable() {
            @Override
            public void run() {
                callback.onLiquidityZonesCreated(formatted);
            }
        });

    }
}

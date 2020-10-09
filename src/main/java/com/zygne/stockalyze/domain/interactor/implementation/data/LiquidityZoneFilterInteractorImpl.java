package com.zygne.stockalyze.domain.interactor.implementation.data;

import com.zygne.stockalyze.domain.MainThread;
import com.zygne.stockalyze.domain.executor.Executor;
import com.zygne.stockalyze.domain.interactor.base.BaseInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.data.base.LiquidityZoneFilterInteractor;
import com.zygne.stockalyze.domain.model.LiquidityZone;
import com.zygne.stockalyze.domain.model.Statistics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LiquidityZoneFilterInteractorImpl extends BaseInteractor implements LiquidityZoneFilterInteractor {

    private final Callback callback;
    private final List<LiquidityZone> data;
    private final Statistics statistics;
    private final double limit;

    public LiquidityZoneFilterInteractorImpl(Executor executor, MainThread mainThread, Callback callback, List<LiquidityZone> data, Statistics statistics, double limit) {
        super(executor, mainThread);
        this.callback = callback;
        this.data = data;
        this.statistics = statistics;
        this.limit = limit;
    }


    @Override
    public void run() {
        List<LiquidityZone> filtered = new ArrayList<>();
        data.sort(new LiquidityZone.PriceComparator());
        Collections.reverse(data);

        filtered.add(data.get(0));

        for (int i = 1; i < data.size(); i++) {
            LiquidityZone e = data.get(i);

            if (e.percentile < limit) {
                filtered.add(e);
            }
        }

        filtered.sort(new LiquidityZone.PriceComparator());
        Collections.reverse(filtered);

        mainThread.post(new Runnable() {
            @Override
            public void run() {
                callback.onLiquidityZonesFiltered(filtered);
            }
        });

    }
}

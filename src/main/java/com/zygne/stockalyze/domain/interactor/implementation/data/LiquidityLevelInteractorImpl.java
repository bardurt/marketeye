package com.zygne.stockalyze.domain.interactor.implementation.data;

import com.zygne.stockalyze.domain.executor.MainThread;
import com.zygne.stockalyze.domain.executor.Executor;
import com.zygne.stockalyze.domain.interactor.base.BaseInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.data.base.LiquidityLevelInteractor;
import com.zygne.stockalyze.domain.model.LiquidityLevel;
import com.zygne.stockalyze.domain.model.VolumePriceSum;

import java.util.ArrayList;
import java.util.List;

public class LiquidityLevelInteractorImpl extends BaseInteractor implements LiquidityLevelInteractor {

    private final Callback callback;
    private final List<VolumePriceSum> data;

    public LiquidityLevelInteractorImpl(Executor executor, MainThread mainThread, Callback callback, List<VolumePriceSum> data) {
        super(executor, mainThread);
        this.callback = callback;
        this.data = data;
    }

    @Override
    public void run() {
        List<LiquidityLevel> formatted = new ArrayList<>();

        for (VolumePriceSum e : data) {
            LiquidityLevel s = new LiquidityLevel(e.price, e.totalSize);
            formatted.add(s);
        }

        formatted.sort(new LiquidityLevel.VolumeComparator());

        int size = formatted.size();

        for (int i = 0; i < formatted.size(); i++) {
            formatted.get(i).setRank(size-1);
            formatted.get(i).setPercentile(((i + 1) / (double) size) * 100);
        }

        mainThread.post(() -> callback.onLiquidityLevelsCreated(formatted));

    }
}

package com.zygne.data.domain.interactor.implementation.data;


import com.zygne.data.domain.interactor.implementation.data.base.LiquidityLevelInteractor;
import com.zygne.data.domain.model.LiquidityLevel;
import com.zygne.data.domain.model.VolumePriceSum;
import com.zygne.arch.domain.executor.Executor;
import com.zygne.arch.domain.executor.MainThread;
import com.zygne.arch.domain.interactor.base.BaseInteractor;

import java.util.ArrayList;
import java.util.Collections;
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
        Collections.reverse(formatted);

        int size = formatted.size();
        double percentile = 100;

        for (int i = 0; i < formatted.size(); i++) {
            percentile = ((size-(i)) / (double) size) * 100d;
            formatted.get(i).setRank(i+1);
            formatted.get(i).setPercentile(percentile);
        }

        mainThread.post(() -> callback.onLiquidityLevelsCreated(formatted));

    }
}

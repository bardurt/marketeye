package com.zygne.stockanalyzer.domain.interactor.implementation.data;

import com.zygne.stockanalyzer.domain.executor.Executor;
import com.zygne.stockanalyzer.domain.executor.MainThread;
import com.zygne.stockanalyzer.domain.interactor.base.BaseInteractor;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.base.LiquidityLevelFilterInteractor;
import com.zygne.stockanalyzer.domain.model.LiquidityLevel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LiquidityLevelFilterInteractorImpl extends BaseInteractor implements LiquidityLevelFilterInteractor {

    private final Callback callback;
    private final List<LiquidityLevel> data;
    private final double limit;

    public LiquidityLevelFilterInteractorImpl(Executor executor, MainThread mainThread, Callback callback, List<LiquidityLevel> data, double limit) {
        super(executor, mainThread);
        this.callback = callback;
        this.data = data;
        this.limit = limit;
    }


    @Override
    public void run() {
        List<LiquidityLevel> filtered = new ArrayList<>();
        data.sort(new LiquidityLevel.PriceComparator());
        Collections.reverse(data);

        for (int i = 1; i < data.size(); i++) {
            LiquidityLevel e = data.get(i);

            if (e.getPercentile() > limit) {
                filtered.add(e);
            }
        }

        filtered.sort(new LiquidityLevel.PriceComparator());
        Collections.reverse(filtered);

        mainThread.post(() -> callback.onLiquidityLevelsFiltered(filtered));

    }
}

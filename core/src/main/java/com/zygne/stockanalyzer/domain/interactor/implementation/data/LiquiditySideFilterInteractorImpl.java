package com.zygne.stockanalyzer.domain.interactor.implementation.data;

import com.zygne.stockanalyzer.domain.executor.Executor;
import com.zygne.stockanalyzer.domain.executor.MainThread;
import com.zygne.stockanalyzer.domain.interactor.base.BaseInteractor;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.base.LiquiditySideFilterInteractor;
import com.zygne.stockanalyzer.domain.model.LiquiditySide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LiquiditySideFilterInteractorImpl extends BaseInteractor implements LiquiditySideFilterInteractor {

    private final Callback callback;
    private final List<LiquiditySide> data;
    private final double percentile;

    public LiquiditySideFilterInteractorImpl(Executor executor, MainThread mainThread, Callback callback, List<LiquiditySide> data, double percentile) {
        super(executor, mainThread);
        this.callback = callback;
        this.data = data;
        this.percentile = percentile;
    }

    @Override
    public void run() {

        List<LiquiditySide> filtered = new ArrayList<>();

        for (int i = 1; i < data.size(); i++) {
            LiquiditySide e = data.get(i);

            if (e.percentile > percentile) {
                filtered.add(e);
            }
        }

        filtered.sort(new LiquiditySide.TimeComparator());

        mainThread.post(() -> callback.onLiquiditySidesFiltered(filtered));


    }
}

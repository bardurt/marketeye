package com.zygne.stockanalyzer.domain.interactor.implementation.data;

import com.zygne.stockanalyzer.domain.executor.Executor;
import com.zygne.stockanalyzer.domain.executor.MainThread;
import com.zygne.stockanalyzer.domain.interactor.base.BaseInteractor;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.base.LiquiditySidePriceInteractor;
import com.zygne.stockanalyzer.domain.model.LiquiditySide;

import java.util.ArrayList;
import java.util.List;

public class LiquiditySidePriceInteractorImpl extends BaseInteractor implements LiquiditySidePriceInteractor {

    private final Callback callback;
    private final List<LiquiditySide> data;
    private final double priceMin;
    private double priceMax;


    public LiquiditySidePriceInteractorImpl(Executor executor, MainThread mainThread, Callback callback, List<LiquiditySide> data, double priceMin, double priceMax) {
        super(executor, mainThread);
        this.callback = callback;
        this.data = data;
        this.priceMin = priceMin;
        this.priceMax = priceMax;
    }

    @Override
    public void run() {

        if (priceMax <= 0) {
            priceMax = Double.MAX_VALUE;
        }

        List<LiquiditySide> filtered = new ArrayList<>();

        for (LiquiditySide e : data) {
            boolean added = false;
            if (e.inZone(priceMin) || e.inZone(priceMax)) {
                filtered.add(e);
                added = true;
            }

            if (!added) {
                if (e.start > priceMin) {
                    if (e.end <= priceMax) {
                        filtered.add(e);
                        added = true;
                    }
                }
            }

        }


        mainThread.post(() -> callback.onLiquiditySideForPriceFound(filtered));

    }
}

package com.zygne.stockanalyzer.domain.interactor.implementation.data;

import com.zygne.stockanalyzer.domain.executor.Executor;
import com.zygne.stockanalyzer.domain.executor.MainThread;
import com.zygne.stockanalyzer.domain.interactor.base.BaseInteractor;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.base.LiquiditySidePriceInteractor;
import com.zygne.stockanalyzer.domain.model.LiquiditySide;

import java.util.ArrayList;
import java.util.List;

public class LiquiditySidePriceInteractorImpl extends BaseInteractor implements LiquiditySidePriceInteractor {

    private Callback callback;
    private List<LiquiditySide> data;
    private double price;

    public LiquiditySidePriceInteractorImpl(Executor executor, MainThread mainThread, Callback callback, List<LiquiditySide> data, double price) {
        super(executor, mainThread);
        this.callback = callback;
        this.data = data;
        this.price = price;
    }

    @Override
    public void run() {

        List<LiquiditySide> filtered = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            LiquiditySide e = data.get(i);

            if (e.inZone(price)){
                filtered.add(e);
            }
        }

        mainThread.post(() -> callback.onLiquiditySideForPriceFound(filtered));

    }
}

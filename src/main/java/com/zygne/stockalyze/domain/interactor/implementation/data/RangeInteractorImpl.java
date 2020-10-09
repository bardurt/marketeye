package com.zygne.stockalyze.domain.interactor.implementation.data;

import com.zygne.stockalyze.domain.MainThread;
import com.zygne.stockalyze.domain.executor.Executor;
import com.zygne.stockalyze.domain.interactor.base.BaseInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.data.base.RangeInteractor;
import com.zygne.stockalyze.domain.model.LiquidityZone;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RangeInteractorImpl extends BaseInteractor implements RangeInteractor {

    private Callback callback;
    private List<LiquidityZone> data;
    private int currentPrice;

    public RangeInteractorImpl(Executor executor, MainThread mainThread, Callback callback, List<LiquidityZone> data, int currentPrice) {
        super(executor, mainThread);
        this.callback = callback;
        this.data = data;
        this.currentPrice = currentPrice;
    }

    @Override
    public void run() {

        List<LiquidityZone> range = new ArrayList<>();

        if (currentPrice == 0) {
            mainThread.post(new Runnable() {
                @Override
                public void run() {
                    callback.onRangeGenerated(range);
                }
            });

            return;
        }


        int originIndex = -1;

        int index = 0;
        for (LiquidityZone e : data) {
            if (e.price >= currentPrice) {
                originIndex = index;
            } else {
                break;
            }
            index++;
        }

        if (originIndex == -1) {
            mainThread.post(new Runnable() {
                @Override
                public void run() {
                    callback.onRangeGenerated(range);
                }
            });
            return;
        }

        int count = 0;
        for (int i = originIndex; i > 0; i--) {
            range.add(data.get(i));
            count++;
            if (count > 15) {
                break;
            }

        }

        count = 0;
        for (int i = originIndex + 1; i < data.size(); i++) {
            range.add(data.get(i));
            count++;
            if (count > 6) {
                break;
            }

        }

        LiquidityZone origin = new LiquidityZone(currentPrice, 0, 0);
        origin.origin = true;
        range.add(origin);
        range.sort(new LiquidityZone.PriceComparator());
        Collections.reverse(range);

        mainThread.post(() -> callback.onRangeGenerated(range));

    }
}

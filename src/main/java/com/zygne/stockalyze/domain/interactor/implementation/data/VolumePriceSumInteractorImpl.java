package com.zygne.stockalyze.domain.interactor.implementation.data;

import com.zygne.stockalyze.domain.executor.MainThread;
import com.zygne.stockalyze.domain.executor.Executor;
import com.zygne.stockalyze.domain.interactor.base.BaseInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.data.base.VolumePriceSumInteractor;
import com.zygne.stockalyze.domain.model.VolumePrice;
import com.zygne.stockalyze.domain.model.VolumePriceSum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VolumePriceSumInteractorImpl extends BaseInteractor implements VolumePriceSumInteractor {

    private final Callback callback;
    private final List<VolumePrice> data;

    public VolumePriceSumInteractorImpl(Executor executor, MainThread mainThread, Callback callback, List<VolumePrice> data) {
        super(executor, mainThread);
        this.callback = callback;
        this.data = data;
    }


    @Override
    public void run() {
        Map<String, VolumePriceSum> map = new HashMap<>();

        for (VolumePrice e : data) {

            String tag = "p" + e.price;

            if (map.get(tag) != null) {
                map.get(tag).totalSize += e.size;
            } else {
                map.put(tag, new VolumePriceSum(e.price, e.size));
            }
        }

        List<VolumePriceSum> groups = new ArrayList<>(map.values());

        mainThread.post(() -> callback.onVolumePriceSumCreated(groups));

    }
}

package com.zygne.stockanalyzer.domain.interactor.implementation.data;

import com.zygne.stockanalyzer.domain.executor.Executor;
import com.zygne.stockanalyzer.domain.executor.MainThread;
import com.zygne.stockanalyzer.domain.interactor.base.BaseInteractor;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.base.VolumePriceSumInteractor;
import com.zygne.stockanalyzer.domain.model.VolumePrice;
import com.zygne.stockanalyzer.domain.model.VolumePriceSum;

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

            String tag = "p" + e.getPrice();

            if (map.get(tag) != null) {
                map.get(tag).totalSize += e.getSize();
                map.get(tag).hits += 1;
            } else {
                VolumePriceSum vps = new VolumePriceSum(e.getPrice(), e.getSize());
                vps.hits = 1;
                map.put(tag, vps);
            }
        }

        List<VolumePriceSum> groups = new ArrayList<>(map.values());

        mainThread.post(() -> callback.onVolumePriceSumCreated(groups));

    }
}

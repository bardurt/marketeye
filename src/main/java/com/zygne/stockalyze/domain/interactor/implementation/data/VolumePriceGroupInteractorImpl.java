package com.zygne.stockalyze.domain.interactor.implementation.data;

import com.zygne.stockalyze.domain.executor.MainThread;
import com.zygne.stockalyze.domain.executor.Executor;
import com.zygne.stockalyze.domain.interactor.base.BaseInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.data.base.VolumePriceGroupInteractor;
import com.zygne.stockalyze.domain.model.VolumePrice;
import com.zygne.stockalyze.domain.model.VolumePriceGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VolumePriceGroupInteractorImpl extends BaseInteractor implements VolumePriceGroupInteractor {

    private final Callback callback;
    private final List<VolumePrice> data;

    public VolumePriceGroupInteractorImpl(Executor executor, MainThread mainThread, Callback callback, List<VolumePrice> data) {
        super(executor, mainThread);
        this.callback = callback;
        this.data = data;
    }


    @Override
    public void run() {
        Map<String, VolumePriceGroup> map = new HashMap<>();

        for (VolumePrice e : data) {

            String tag = "p" + e.price;

            if (map.get(tag) != null) {
                map.get(tag).orderCount++;
                map.get(tag).totalSize += e.size;
            } else {
                map.put(tag, new VolumePriceGroup(e.price, e.size));
            }
        }

        List<VolumePriceGroup> groups = new ArrayList<>(map.values());

        mainThread.post(() -> callback.onVolumePriceGroupCreated(groups));

    }
}

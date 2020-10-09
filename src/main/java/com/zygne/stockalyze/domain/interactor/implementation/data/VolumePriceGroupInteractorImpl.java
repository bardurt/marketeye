package com.zygne.stockalyze.domain.interactor.implementation.data;

import com.zygne.stockalyze.domain.MainThread;
import com.zygne.stockalyze.domain.executor.Executor;
import com.zygne.stockalyze.domain.interactor.base.BaseInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.data.base.VolumePriceGroupInteractor;
import com.zygne.stockalyze.domain.model.VolumePriceGroup;
import com.zygne.stockalyze.domain.model.VolumePriceLevel;

import java.util.*;

public class VolumePriceGroupInteractorImpl extends BaseInteractor implements VolumePriceGroupInteractor {

    private final Callback callback;
    private final List<VolumePriceLevel> data;

    public VolumePriceGroupInteractorImpl(Executor executor, MainThread mainThread, Callback callback, List<VolumePriceLevel> data) {
        super(executor, mainThread);
        this.callback = callback;
        this.data = data;
    }


    @Override
    public void run() {
        Map<String, VolumePriceGroup> map = new HashMap<>();

        for (VolumePriceLevel e : data) {

            String tag = "p" + e.price;

            if (map.get(tag) != null) {
                map.get(tag).orderCount++;
                map.get(tag).totalSize += e.size;
            } else {
                map.put(tag, new VolumePriceGroup(e.price, e.size));
            }
        }

        List<VolumePriceGroup> groups = new ArrayList<>(map.values());

        Collections.sort(groups);
        Collections.reverse(groups);

        mainThread.post(new Runnable() {
            @Override
            public void run() {
                callback.onVolumePriceGroupCreated(groups);
            }
        });

    }
}

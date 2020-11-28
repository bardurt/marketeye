package com.zygne.stockanalyzer.domain.interactor.implementation.data;

import com.zygne.stockanalyzer.domain.executor.Executor;
import com.zygne.stockanalyzer.domain.executor.MainThread;
import com.zygne.stockanalyzer.domain.interactor.base.BaseInteractor;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.base.VolumePriceInteractor;
import com.zygne.stockanalyzer.domain.model.Histogram;
import com.zygne.stockanalyzer.domain.model.VolumePrice;

import java.util.ArrayList;
import java.util.List;

public class VolumePriceInteractorImpl extends BaseInteractor implements VolumePriceInteractor {

    private final Callback callback;
    private final List<Histogram> histogramList;
    private final int rule;

    public VolumePriceInteractorImpl(Executor executor, MainThread mainThread, Callback callback, List<Histogram> histogramList, int rule) {
        super(executor, mainThread);
        this.callback = callback;
        this.histogramList = histogramList;
        this.rule = rule;
    }

    @Override
    public void run() {
        List<VolumePrice> data;

        if (rule == 0) {
            data = high();
        } else {
            data = low();
        }

        List<VolumePrice> finalData = data;
        mainThread.post(() -> callback.onVolumePriceCreated(finalData));
    }


    private List<VolumePrice> high() {
        List<VolumePrice> data = new ArrayList<>();
        for (Histogram e : histogramList) {

            VolumePrice vp1 = new VolumePrice(e.high, e.volume);
            data.add(vp1);
        }

        return data;
    }

    private List<VolumePrice> low() {
        List<VolumePrice> data = new ArrayList<>();
        for (Histogram e : histogramList) {

            VolumePrice vp1 = new VolumePrice(e.low, e.volume);
            data.add(vp1);
        }

        return data;
    }

}

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
        } else if(rule == 1) {
            data = low();
        } else {
            data = ohlc();
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

    private List<VolumePrice> ohlc() {
        List<VolumePrice> data = new ArrayList<>();
        for (Histogram e : histogramList) {


            VolumePrice vp1 = new VolumePrice(e.open, (long) (e.volume * 0.20));
            data.add(vp1);

            VolumePrice vp2 = new VolumePrice(e.high, (long) (e.volume * 0.35));
            data.add(vp2);

            VolumePrice vp3 = new VolumePrice(e.low, (long) (e.volume * 0.25));
            data.add(vp3);

            VolumePrice vp4 = new VolumePrice(e.close, (long) (e.volume * 0.20));
            data.add(vp4);
        }

        return data;
    }

}

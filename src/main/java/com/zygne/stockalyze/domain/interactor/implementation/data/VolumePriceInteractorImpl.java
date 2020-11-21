package com.zygne.stockalyze.domain.interactor.implementation.data;

import com.zygne.stockalyze.domain.executor.MainThread;
import com.zygne.stockalyze.domain.executor.Executor;
import com.zygne.stockalyze.domain.interactor.base.BaseInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.data.base.VolumePriceInteractor;
import com.zygne.stockalyze.domain.model.Histogram;
import com.zygne.stockalyze.domain.model.VolumePrice;

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

        System.out.println("VolumePriceInteractorImpl");

        if (rule == 0) {
            data = high();
        } else if (rule == 1) {
            data = low();
        } else if (rule == 2) {
            data = ohlc();
        } else {
            data = ohlcm();
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

    private List<VolumePrice> ohlcm() {
        List<VolumePrice> data = new ArrayList<>();
        for (Histogram e : histogramList) {

            long volume = (long) (e.volume * e.decay);

            VolumePrice vp1 = new VolumePrice(e.open, (long) (volume * 0.25));
            VolumePrice vp2 = new VolumePrice(e.high, (long) (volume * 0.2));
            VolumePrice vp3 = new VolumePrice(e.low, (long) (volume * 0.2));
            VolumePrice vp4 = new VolumePrice(e.close, (long) (volume * 0.25));

            double middle = (e.high - e.low) / 2;
            VolumePrice vp5 = new VolumePrice(middle, (long) (volume * 0.1));

            data.add(vp1);
            data.add(vp2);
            data.add(vp3);
            data.add(vp4);
            data.add(vp5);
        }

        return data;
    }

    private List<VolumePrice> ohlc() {
        List<VolumePrice> data = new ArrayList<>();
        for (Histogram e : histogramList) {

            long volume = (long) (e.volume * e.decay);

            VolumePrice vp1 = new VolumePrice(e.open, (long) (volume * 0.25));
            VolumePrice vp2 = new VolumePrice(e.high, (long) (volume * 0.25));
            VolumePrice vp3 = new VolumePrice(e.low, (long) (volume * 0.25));
            VolumePrice vp4 = new VolumePrice(e.close, (long) (volume * 0.25));

            data.add(vp1);
            data.add(vp2);
            data.add(vp3);
            data.add(vp4);
        }

        return data;
    }

}

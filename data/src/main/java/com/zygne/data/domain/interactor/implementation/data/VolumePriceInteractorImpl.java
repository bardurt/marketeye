package com.zygne.data.domain.interactor.implementation.data;

import com.zygne.data.domain.interactor.implementation.data.base.VolumePriceInteractor;
import com.zygne.data.domain.model.Histogram;
import com.zygne.data.domain.model.VolumePrice;
import com.zygne.arch.domain.executor.Executor;
import com.zygne.arch.domain.executor.MainThread;
import com.zygne.arch.domain.interactor.base.BaseInteractor;

import java.util.ArrayList;
import java.util.List;

public class VolumePriceInteractorImpl extends BaseInteractor implements VolumePriceInteractor {

    private final Callback callback;
    private final List<Histogram> histogramList;
    private final PriceStructure priceStructure;

    public VolumePriceInteractorImpl(Executor executor, MainThread mainThread, Callback callback, List<Histogram> histogramList, PriceStructure priceStructure) {
        super(executor, mainThread);
        this.callback = callback;
        this.histogramList = histogramList;
        this.priceStructure = priceStructure;
    }

    @Override
    public void run() {
        List<VolumePrice> data = new ArrayList<>();

        switch (priceStructure){
            case H:
                data = high();
                break;
            case HL:
                data = high();
                break;
            case OHLC:
                data = ohlc();
                break;
            case OHLCM:
                data = ohlcm();
            case SECTION:
                data = section();
                break;
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

    private List<VolumePrice> ohlcm() {
        List<VolumePrice> data = new ArrayList<>();
        for (Histogram e : histogramList) {


            VolumePrice vp1 = new VolumePrice(e.open, (long) (e.volume * 0.25));
            data.add(vp1);

            VolumePrice vp2 = new VolumePrice(e.high, (long) (e.volume * 0.15));
            data.add(vp2);

            VolumePrice vp3 = new VolumePrice(e.low, (long) (e.volume * 0.15));
            data.add(vp3);

            VolumePrice vp4 = new VolumePrice(e.close, (long) (e.volume * 0.25));
            data.add(vp4);

            VolumePrice vp5 = new VolumePrice((e.low + e.high)/2, (long) (e.volume * 0.20));
            data.add(vp5);
        }

        return data;
    }

    private List<VolumePrice> section() {
        List<VolumePrice> data = new ArrayList<>();

        int sections =  10;
        double min  = 0.01d;
        for (Histogram e : histogramList) {

            double size = e.getTotalRange() / sections;
            if(size > min){

                double price = e.low;

                while (price < e.high){

                    VolumePrice vp = new VolumePrice(e.open, (long) (e.volume / sections));
                    data.add(vp);

                    price += size;

                }

            } else {
                VolumePrice vp1 = new VolumePrice(e.high, (long) (e.volume * 0.20));
                data.add(vp1);
            }
        }

        return data;
    }

}

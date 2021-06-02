package com.zygne.stockanalyzer.domain.interactor.implementation.data;

import com.zygne.stockanalyzer.domain.executor.Executor;
import com.zygne.stockanalyzer.domain.executor.MainThread;
import com.zygne.stockanalyzer.domain.interactor.base.BaseInteractor;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.base.VolumePriceInteractor;
import com.zygne.stockanalyzer.domain.model.Histogram;
import com.zygne.stockanalyzer.domain.model.VolumePrice;
import com.zygne.stockanalyzer.domain.utils.NumberHelper;

import java.util.ArrayList;
import java.util.List;

import static com.zygne.stockanalyzer.domain.utils.NumberHelper.roundUp;

public class VolumePriceBinInteractorImpl extends BaseInteractor implements VolumePriceInteractor {

    private final Callback callback;
    private final List<Histogram> histogramList;

    double[] priceGrops = new double[]{0.05, 0.1, 0.25, 0.5, 1};


    public VolumePriceBinInteractorImpl(Executor executor, MainThread mainThread, Callback callback, List<Histogram> histogramList, int rule) {
        super(executor, mainThread);
        this.callback = callback;
        this.histogramList = histogramList;
    }

    @Override
    public void run() {
        List<VolumePrice> data = new ArrayList<>();

        int index = 0;

        double rounding = priceGrops[index];

        while(true) {
            data.clear();

            for (Histogram e : histogramList) {
                double price = NumberHelper.round2Decimals(roundUp(e.high, rounding));
                VolumePrice vp1 = new VolumePrice(price, e.volume);
                data.add(vp1);
            }

            if(data.size() < 50){
                break;
            } else {
                index++;
                if(index > priceGrops.length -1){
                    break;
                }

                rounding = priceGrops[index];
            }

        }

        List<VolumePrice> finalData = data;
        mainThread.post(() -> callback.onVolumePriceCreated(finalData));
    }


}
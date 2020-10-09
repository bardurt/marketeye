package com.zygne.stockalyze.domain.interactor.implementation.data;

import com.zygne.stockalyze.domain.MainThread;
import com.zygne.stockalyze.domain.executor.Executor;
import com.zygne.stockalyze.domain.interactor.base.BaseInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.data.base.VolumePriceInteractor;
import com.zygne.stockalyze.domain.model.Histogram;
import com.zygne.stockalyze.domain.model.VolumePriceLevel;
import com.zygne.stockalyze.ui.fx.Main;

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
        List<VolumePriceLevel> data;

        if(rule == 0){
            data = onePoint();
        } else if(rule == 1){
            data = fourPoint();
        } else {
            data = fivePoint();
        }

        List<VolumePriceLevel> finalData = data;
        mainThread.post(() -> callback.onVolumePriceCreated(finalData));
    }


    private List<VolumePriceLevel> onePoint(){
        List<VolumePriceLevel> data = new ArrayList<>();
        for(Histogram e : histogramList){

            VolumePriceLevel vp1 = new VolumePriceLevel(e.high, e.volume);
            data.add(vp1);
        }

        return data;
    }

    private List<VolumePriceLevel> fivePoint(){
        List<VolumePriceLevel> data = new ArrayList<>();
        for(Histogram e : histogramList){

            long volume = (long) (e.volume * e.decay);

            VolumePriceLevel vp1 = new VolumePriceLevel(e.open, (long) (volume * 0.25));
            VolumePriceLevel vp2 = new VolumePriceLevel(e.high, (long) (volume * 0.2));
            VolumePriceLevel vp3 = new VolumePriceLevel(e.low, (long) (volume * 0.2));
            VolumePriceLevel vp4 = new VolumePriceLevel(e.close, (long) (volume * 0.25));

            int middle = (e.high - e.low) / 2;
            VolumePriceLevel vp5 = new VolumePriceLevel(middle, (long) (volume * 0.1));

            data.add(vp1);
            data.add(vp2);
            data.add(vp3);
            data.add(vp4);
            data.add(vp5);
        }

        return data;
    }

    private List<VolumePriceLevel> fourPoint(){
        List<VolumePriceLevel> data = new ArrayList<>();
        for(Histogram e : histogramList){

            long volume = (long) (e.volume * e.decay);

            VolumePriceLevel vp1 = new VolumePriceLevel(e.open, (long) (volume * 0.25));
            VolumePriceLevel vp2 = new VolumePriceLevel(e.high, (long) (volume * 0.25));
            VolumePriceLevel vp3 = new VolumePriceLevel(e.low, (long) (volume * 0.25));
            VolumePriceLevel vp4 = new VolumePriceLevel(e.close, (long) (volume * 0.25));

            data.add(vp1);
            data.add(vp2);
            data.add(vp3);
            data.add(vp4);
        }

        return data;
    }

}

package com.zygne.stockanalyzer.domain.interactor.implementation.data;

import com.zygne.stockanalyzer.domain.executor.Executor;
import com.zygne.stockanalyzer.domain.executor.MainThread;
import com.zygne.stockanalyzer.domain.interactor.base.BaseInteractor;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.base.VolumeBarDetailsInteractor;
import com.zygne.stockanalyzer.domain.model.Histogram;
import com.zygne.stockanalyzer.domain.model.VolumeBarDetails;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VolumeBarDetailsInteractorImpl extends BaseInteractor implements VolumeBarDetailsInteractor {


    private Callback callback;
    private List<Histogram> histograms;

    public VolumeBarDetailsInteractorImpl(Executor executor, MainThread mainThread, Callback callback, List<Histogram> histograms) {
        super(executor, mainThread);
        this.callback = callback;
        this.histograms = histograms;
    }

    @Override
    public void run() {

        List<VolumeBarDetails> details = new ArrayList<>();


        histograms.sort(new Histogram.VolumeComparator());
        Collections.reverse(histograms);

        int count = 1;

        for(Histogram h : histograms){

            VolumeBarDetails volumeBarDetails = new VolumeBarDetails();

            volumeBarDetails.setHigh(h.high);
            volumeBarDetails.setLow(h.low);
            volumeBarDetails.setRank(count);
            volumeBarDetails.setVolume(h.volume);
            volumeBarDetails.setTimeStamp(h.timeStamp);

            details.add(volumeBarDetails);
            count++;

            if(count > 10){
                break;
            }
        }


        mainThread.post(() -> callback.onVolumeBarDetailsCreated(details));

    }
}

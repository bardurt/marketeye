package com.zygne.stockalyze.domain.interactor.implementation.data;

import com.zygne.stockalyze.domain.MainThread;
import com.zygne.stockalyze.domain.executor.Executor;
import com.zygne.stockalyze.domain.interactor.base.BaseInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.data.base.GapRateInteractor;
import com.zygne.stockalyze.domain.model.GapResult;
import com.zygne.stockalyze.domain.model.Histogram;

import java.util.List;

public class GapRateInteractorImpl extends BaseInteractor implements GapRateInteractor {

    private static final double LIMIT = 1.1d;

    private Callback callback;
    private List<Histogram> data;
    private double limit;

    public GapRateInteractorImpl(Executor executor, MainThread mainThread, Callback callback,
                                 List<Histogram> data, double limit) {
        super(executor, mainThread);
        this.callback = callback;
        this.data = data;
        this.limit =  1 + (limit/100);
    }

    @Override
    public void run() {

        GapResult gapResult = new GapResult();
        gapResult.gapLimit = limit;

        double oCminRange = 100;
        double oCmaxRange = 0;
        double lHminRange = 100;
        double lHmaxRange = 0;

        data.sort(new Histogram.TimeComparator());

        for (int i = 1; i < data.size(); i++) {

            Histogram histogram1 = data.get(i - 1);
            Histogram histogram2 = data.get(i);

            // Check if it is gapping
            if (histogram2.open > histogram1.close) {
                if (histogram2.open / (double) histogram1.close > limit) {

                    gapResult.gapCount++;

                    if(histogram2.getDirection() == Histogram.Direction.Up){
                        gapResult.gapBull++;
                    }

                    if(histogram2.getBodyRange() > oCmaxRange){
                        oCmaxRange = histogram2.getBodyRange();
                        gapResult.openCloseRange.max = histogram2.getBodyRange();
                    }

                    if(histogram2.getBodyRange() < oCminRange){
                        oCminRange = histogram2.getBodyRange();
                        gapResult.openCloseRange.min = histogram2.getBodyRange();
                    }

                    if(histogram2.getTotalRange() > lHmaxRange){
                        lHmaxRange = histogram2.getTotalRange();
                        gapResult.lowHighRange.max = histogram2.getTotalRange();
                    }

                    if(histogram2.getTotalRange() < lHminRange){
                        lHminRange = histogram2.getTotalRange();
                        gapResult.lowHighRange.min = histogram2.getTotalRange();
                    }

                    gapResult.openHighRange.sum += histogram2.getOpenHighRange();
                    gapResult.openHighRange.count++;

                    gapResult.lowHighRange.sum += histogram2.getTotalRange();
                    gapResult.lowHighRange.count++;

                    gapResult.openCloseRange.sum += histogram2.getBodyRange();
                    gapResult.openCloseRange.count++;

                }
            }
        }

        gapResult.openHighRange.calculateAverage();
        gapResult.lowHighRange.calculateAverage();
        gapResult.openCloseRange.calculateAverage();

        mainThread.post(() -> callback.onGapRateCalculated(gapResult));
    }
}

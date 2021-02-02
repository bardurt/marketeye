package com.zygne.stockanalyzer.domain.interactor.implementation.data;

import com.zygne.stockanalyzer.domain.executor.Executor;
import com.zygne.stockanalyzer.domain.executor.MainThread;
import com.zygne.stockanalyzer.domain.interactor.base.BaseInteractor;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.base.HistogramDayFilterInteractor;
import com.zygne.stockanalyzer.domain.model.Histogram;
import com.zygne.stockanalyzer.domain.utils.TimeHelper;

import java.util.ArrayList;
import java.util.List;

public class HistogramDayFilterInteractorImpl extends BaseInteractor implements HistogramDayFilterInteractor {

    private final Callback callback;
    private final List<Histogram> histogramList;
    private final long timeStamp;

    public HistogramDayFilterInteractorImpl(Executor executor, MainThread mainThread, Callback callback, List<Histogram> histogramList, long timeStamp) {
        super(executor, mainThread);
        this.callback = callback;
        this.histogramList = histogramList;
        this.timeStamp = timeStamp;
    }

    @Override
    public void run() {

        List<Histogram> filteredList = new ArrayList<>();

        for(Histogram e : histogramList){
            if(TimeHelper.isSameDay(e.timeStamp, timeStamp)){
                filteredList.add(e);
            }
        }

        mainThread.post(() -> callback.onHistogramFiltered(filteredList));
    }
}

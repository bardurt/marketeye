package com.zygne.stockanalyzer.domain.interactor.implementation.data.io;

import com.zygne.stockanalyzer.domain.executor.Executor;
import com.zygne.stockanalyzer.domain.executor.MainThread;
import com.zygne.stockanalyzer.domain.interactor.base.BaseInteractor;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.base.DataMergeInteractor;
import com.zygne.stockanalyzer.domain.model.BarData;

import java.util.List;

public class DataMergeInteractorImpl extends BaseInteractor implements DataMergeInteractor {

    private final Callback callback;
    private final List<BarData> dataNew;
    private final List<BarData > dataOld;


    public DataMergeInteractorImpl(Executor executor, MainThread mainThread, Callback callback, List<BarData> dataNew, List<BarData> dataOld) {
        super(executor, mainThread);
        this.callback = callback;
        this.dataNew = dataNew;
        this.dataOld = dataOld;
    }

    @Override
    public void run() {
        if(dataOld.isEmpty()){
            mainThread.post(() -> callback.onDataMerged(dataNew));
            return;
        }

        for(BarData d : dataNew){
           if(!dataOld.contains(d)){
               dataOld.add(d);
           }
        }

        mainThread.post(() -> callback.onDataMerged(dataOld));
    }
}

package com.zygne.stockalyze.domain.interactor.implementation.data;

import com.zygne.stockalyze.domain.executor.MainThread;
import com.zygne.stockalyze.domain.executor.Executor;
import com.zygne.stockalyze.domain.interactor.base.BaseInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.data.base.DataMergeInteractor;

import java.util.ArrayList;
import java.util.List;

public class DataMergeInteractorImpl extends BaseInteractor implements DataMergeInteractor {

    private final Callback callback;
    private final List<String> dataNew;
    private final List<String > dataOld;


    public DataMergeInteractorImpl(Executor executor, MainThread mainThread, Callback callback, List<String> dataNew, List<String> dataOld) {
        super(executor, mainThread);
        this.callback = callback;
        this.dataNew = dataNew;
        this.dataOld = dataOld;
    }

    @Override
    public void run() {
        if(dataOld.isEmpty()){
            System.out.println("Merging data, old is empty");
            mainThread.post(() -> callback.onDataMerged(dataNew));
            return;
        }


        String latestEntry = dataOld.get(0);

        String timeStamp1 = latestEntry.split(",",-1)[0];

        int splitPoint = -1;

        for(int i = 0; i < dataNew.size(); i++){
            String timeStamp2 = dataNew.get(i).split(",",-1)[0];

            if(timeStamp1.equalsIgnoreCase(timeStamp2)){
                splitPoint = i;
                break;
            }
        }

        List<String> mergedData = new ArrayList<>();

        if(splitPoint > -1){

            for(int i = 0; i < splitPoint; i++){

                mergedData.add(dataNew.get(i));
            }
        }

        mergedData.addAll(dataOld);

        mainThread.post(() -> callback.onDataMerged(mergedData));
    }
}

package com.zygne.stockanalyzer.domain.interactor.implementation.data;

import com.zygne.stockanalyzer.domain.executor.Executor;
import com.zygne.stockanalyzer.domain.executor.MainThread;
import com.zygne.stockanalyzer.domain.interactor.base.BaseInteractor;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.base.DateFilterInteractor;
import com.zygne.stockanalyzer.domain.model.BarData;
import com.zygne.stockanalyzer.domain.model.DataSize;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class DateFilterInteractorImpl extends BaseInteractor implements DateFilterInteractor {

    private Callback callback;
    private List<BarData> data;
    private DataSize dataSize;

    public DateFilterInteractorImpl(Executor executor, MainThread mainThread, Callback callback, List<BarData> data, DataSize dataSize) {
        super(executor, mainThread);
        this.callback = callback;
        this.data = data;
        this.dataSize = dataSize;
    }

    @Override
    public void run() {

        List<BarData> filteredList = new ArrayList<>();

        Collections.sort(data, new BarData.TimeComparator());

        Calendar calendar = Calendar.getInstance();

        if(dataSize.getUnit() == DataSize.Unit.Month) {
            calendar.add(Calendar.MONTH, -dataSize.getSize());
        } else if(dataSize.getUnit() == DataSize.Unit.Year) {
            calendar.add(Calendar.YEAR, -dataSize.getSize());
        } else if(dataSize.getUnit() == DataSize.Unit.Day) {
            calendar.add(Calendar.DATE, -dataSize.getSize());
        }

        long timeEnd = calendar.getTimeInMillis();

        for(BarData e : data){
            if(e.getTimeStamp() > timeEnd){
                filteredList.add(e);
            }
        }

        mainThread.post(() -> callback.onDateFilterCompleted(filteredList));

    }
}

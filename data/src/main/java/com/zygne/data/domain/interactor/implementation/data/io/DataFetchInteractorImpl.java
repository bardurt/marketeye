package com.zygne.data.domain.interactor.implementation.data.io;

import com.zygne.data.domain.DataBroker;
import com.zygne.data.domain.interactor.implementation.data.base.DataFetchInteractor;
import com.zygne.data.domain.model.BarData;
import com.zygne.arch.domain.executor.Executor;
import com.zygne.arch.domain.executor.MainThread;
import com.zygne.arch.domain.interactor.base.BaseInteractor;

import java.util.ArrayList;
import java.util.List;

public class DataFetchInteractorImpl extends BaseInteractor implements DataFetchInteractor, DataBroker.Callback  {

    private final Callback callback;
    private final String symbol;
    private final int years;
    private final DataBroker dataBroker;
    private final List<BarData> data;

    public DataFetchInteractorImpl(Executor executor, MainThread mainThread, Callback callback, String symbol, int yearsToFetch, DataBroker dataBroker) {
        super(executor, mainThread);
        this.callback = callback;
        this.symbol = symbol;
        this.years = yearsToFetch;
        this.dataBroker = dataBroker;
        data = new ArrayList<>();
    }

    @Override
    public void run() {
        dataBroker.setCallback(this);
        dataBroker.downloadHistoricalBarData(symbol, years);
    }

    @Override
    public void onDataFinished(List<BarData> data) {
        dataBroker.removeCallback();
        if(data.isEmpty()){
            mainThread.post(() -> callback.onDataFetchError("No data found for " + symbol));
            return;
        }
        this.data.addAll(data);
        String timeStamp = data.get(data.size()-1).getTime();
        mainThread.post(() -> callback.onDataFetched(data, timeStamp));
    }
}

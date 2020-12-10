package com.zygne.stockanalyzer.domain.interactor.implementation.data.ib;

import com.zygne.stockanalyzer.domain.DataBroker;
import com.zygne.stockanalyzer.domain.executor.Executor;
import com.zygne.stockanalyzer.domain.executor.MainThread;
import com.zygne.stockanalyzer.domain.interactor.base.BaseInteractor;
import com.zygne.stockanalyzer.domain.interactor.implementation.data.base.DataFetchInteractor;
import com.zygne.stockanalyzer.domain.model.BarData;
import com.zygne.stockanalyzer.domain.model.enums.TimeInterval;

import java.util.ArrayList;
import java.util.List;

public class IbPartialDataInteractor extends BaseInteractor implements DataFetchInteractor, DataBroker.Callback {

    private Callback callback;
    private String symbol;
    private TimeInterval timeInterval;
    private int length;
    private DataBroker dataBroker;
    private List<BarData> data;

    public IbPartialDataInteractor(Executor executor, MainThread mainThread, Callback callback, String symbol, TimeInterval timeInterval, int length, DataBroker dataBroker) {
        super(executor, mainThread);
        this.callback = callback;
        this.symbol = symbol;
        this.timeInterval = timeInterval;
        this.length = length;
        this.dataBroker = dataBroker;
        data = new ArrayList<>();
    }

    @Override
    public void run() {

        System.out.println("IB Partial Data for " + length + " days");
        String interval = "1 M";

        switch (timeInterval){
            case One_Minute:
                interval = "1 min";
                break;
            case Three_Minutes:
                interval = "3 mins";
                break;
            case Five_Minutes:
                interval = "5 mins";
                break;
            case Fifteen_Minutes:
                interval = "15 mins";
                break;
            case Thirty_Minutes:
                interval = "30 mins";
                break;
            case Hour:
                interval = "1 hour";
                break;
            case Day:
                interval = "1 day";
                break;
            case Week:
                interval = "1 week";
                break;
            case Month:
                interval = "1 month";
                break;
        }
        String time = length + " D";

        dataBroker.setCallback(this);
        dataBroker.downloadData(symbol, time, interval);

    }

    @Override
    public void onDataFinished(List<BarData> data) {
        dataBroker.removeCallback();
        this.data.addAll(data);
        String timeStamp = data.get(data.size()-1).getTime();
        mainThread.post(() -> callback.onDataFetched(data, timeStamp));
    }
}
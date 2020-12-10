package com.zygne.stockanalyzer.domain;

import com.zygne.stockanalyzer.domain.model.BarData;

import java.util.List;

public interface DataBroker extends Api {

    void downloadData(String symbol, String length, String interval);
    void setCallback(Callback callback);
    void removeCallback();

    interface Callback{
        void onDataFinished(List<BarData> data);
    }
}

package com.zygne.data.domain;

import com.zygne.data.domain.model.BarData;

import java.util.List;

public interface DataBroker  {

    void downloadHistoricalBarData(String symbol, int yearsBack);
    void setCallback(Callback callback);
    void removeCallback();

    interface Callback{
        void onDataFinished(List<BarData> data);
    }
}

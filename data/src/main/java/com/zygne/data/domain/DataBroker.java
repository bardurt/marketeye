package com.zygne.data.domain;

import com.zygne.data.domain.model.BarData;
import com.zygne.data.domain.model.DataSize;
import com.zygne.data.domain.model.enums.TimeInterval;

import java.util.List;

public interface DataBroker  {

    void downloadHistoricalBarData(String symbol, DataSize dataSize, TimeInterval timeInterval, boolean yearStart);
    void setCallback(Callback callback);
    void removeCallback();

    interface Callback{
        void onDataFinished(List<BarData> data);
        void onTickPriceFetched(double price);
    }

    enum Asset{
        Stock,
        Crypto
    }
}

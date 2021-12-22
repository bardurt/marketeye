package com.zygne.stockanalyzer.domain.api;

import com.zygne.stockanalyzer.domain.model.BarData;
import com.zygne.stockanalyzer.domain.model.DataSize;
import com.zygne.stockanalyzer.domain.model.enums.TimeInterval;

import java.util.List;

public interface DataBroker extends Api {

    void getLastTickPrice(String symbol);
    void downloadHistoricalBarData(String symbol, DataSize dataSize, TimeInterval timeInterval);
    void setCallback(Callback callback);
    void removeCallback();
    void setAsset(Asset asset);

    interface Callback{
        void onDataFinished(List<BarData> data);
        void onTickPriceFetched(double price);
    }

    enum Asset{
        Stock,
        Crypto
    }
}

package com.zygne.data.domain;

import java.util.List;

public interface DataBroker  {

    void downloadData(String symbol, int yearsBack);
    void setCallback(Callback callback);
    void removeCallback();

    interface Callback{
        void onDataFinished(List<FinanceData> data);
    }
}

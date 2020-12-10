package com.zygne.stockanalyzer.domain.interactor.implementation.data.base;


import com.zygne.stockanalyzer.domain.interactor.base.Interactor;
import com.zygne.stockanalyzer.domain.model.BarData;

import java.util.List;

public interface DataFetchInteractor extends Interactor {

    interface Callback{
        void onDataFetched(List<BarData> entries, String timestamp);
        void onDataFetchError(String message);
        void onStatusUpdate(String message);
    }
}

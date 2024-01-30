package com.zygne.data.domain.interactor.implementation.data.base;


import com.zygne.data.domain.model.BarData;
import com.zygne.arch.domain.interactor.base.Interactor;

import java.util.List;

public interface DataFetchInteractor extends Interactor {

    interface Callback{
        void onDataFetched(List<BarData> entries, String timestamp);
        void onDataFetchError(String message);
        void onStatusUpdate(String message);
    }
}

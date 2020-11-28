package com.zygne.stockanalyzer.domain.interactor.implementation.data.base;


import com.zygne.stockanalyzer.domain.interactor.base.Interactor;

import java.util.List;

public interface DataFetchInteractor extends Interactor {

    interface Callback{
        void onDataFetched(List<String> entries);
        void onDataFetchError(String message);
        void onStatusUpdate(String message);
    }
}

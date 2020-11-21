package com.zygne.stockalyze.domain.interactor.implementation.data.base;


import com.zygne.stockalyze.domain.interactor.base.Interactor;
import com.zygne.stockalyze.domain.model.enums.TimeFrame;

import java.util.List;

public interface DataFetchInteractor extends Interactor {

    interface Callback{
        void onDataFetched(List<String> entries, String ticker, TimeFrame timeFrame);
        void onDataFetchError(String message);
    }
}

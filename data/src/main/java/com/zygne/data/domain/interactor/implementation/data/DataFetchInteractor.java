package com.zygne.data.domain.interactor.implementation.data;


import com.zygne.data.domain.FinanceData;
import com.zygne.data.domain.model.BarData;
import com.zygne.arch.domain.interactor.base.Interactor;

import java.util.List;

public interface DataFetchInteractor extends Interactor {

    interface Callback{
        void onDataFetched(List<FinanceData> entries);
        void onDataFetchError(String message);
    }
}

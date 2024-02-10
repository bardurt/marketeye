package com.zygne.data.domain.interactor.implementation.data.base;

import com.zygne.arch.domain.interactor.base.Interactor;
import com.zygne.data.domain.FinanceData;
import com.zygne.data.domain.model.CotData;

import java.util.List;

public interface CotInteractor extends Interactor {

    interface Callback{
        void onCotDataLoaded(List<CotData> entries);
        void onError();
    }
}

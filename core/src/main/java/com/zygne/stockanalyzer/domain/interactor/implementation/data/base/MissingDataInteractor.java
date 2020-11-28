package com.zygne.stockanalyzer.domain.interactor.implementation.data.base;

import com.zygne.stockanalyzer.domain.interactor.base.Interactor;

public interface MissingDataInteractor extends Interactor {

    interface Callback{
        void onMissingDataCalculated(int daysMissing);
    }
}

package com.zygne.stockalyze.domain.interactor.implementation.data.base;

import com.zygne.stockalyze.domain.interactor.base.Interactor;

public interface MissingDataInteractor extends Interactor {

    interface Callback{
        void onMissingDataCalculated(int daysMissing);
    }
}

package com.zygne.stockanalyzer.domain.interactor.implementation.data.base;

import com.zygne.stockanalyzer.domain.interactor.base.Interactor;

public interface CacheCheckerInteractor extends Interactor {

    interface Callback{
        void onCachedDataFound(String location);
        void onCachedDataError();
    }
}

package com.zygne.stockalyze.domain.interactor.implementation.data.base;

import com.zygne.stockalyze.domain.interactor.base.Interactor;

public interface CacheCheckerInteractor extends Interactor {

    public interface Callback{
        void onCachedDataFound(String location);
        void onCachedDataError();
    }
}

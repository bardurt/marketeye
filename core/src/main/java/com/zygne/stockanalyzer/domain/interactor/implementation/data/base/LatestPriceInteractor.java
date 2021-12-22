package com.zygne.stockanalyzer.domain.interactor.implementation.data.base;

import com.zygne.stockanalyzer.domain.interactor.base.Interactor;

public interface LatestPriceInteractor extends Interactor {

    interface Callback{
        void onLatestPriceFetched(double price);
    }
}

package com.zygne.stockalyze.domain.interactor.implementation.data.base;

public interface MarketPriceInteractor {

    interface Callback{
        void onMarketPriceFetched(double price);
    }
}

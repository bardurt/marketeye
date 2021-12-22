package com.zygne.stockanalyzer.domain.interactor.implementation.data.base;

import com.zygne.stockanalyzer.domain.interactor.base.Interactor;
import com.zygne.stockanalyzer.domain.model.PriceImbalance;

import java.util.List;

public interface PriceImbalanceInteractor extends Interactor {

    interface Callback{
        void onPriceImbalanceCompleted(List<PriceImbalance> data);
    }
}

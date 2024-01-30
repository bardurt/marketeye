package com.zygne.data.domain.interactor.implementation.data.base;

import com.zygne.data.domain.model.PriceImbalance;
import com.zygne.arch.domain.interactor.base.Interactor;

import java.util.List;

public interface PriceImbalanceInteractor extends Interactor {

    interface Callback{
        void onPriceImbalanceCompleted(List<PriceImbalance> data);
    }
}

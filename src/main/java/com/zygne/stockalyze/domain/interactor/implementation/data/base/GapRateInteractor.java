package com.zygne.stockalyze.domain.interactor.implementation.data.base;

import com.zygne.stockalyze.domain.interactor.base.Interactor;
import com.zygne.stockalyze.domain.model.GapResult;

public interface GapRateInteractor extends Interactor {

    interface Callback{
        void onGapRateCalculated(GapResult gapResult);
    }
}

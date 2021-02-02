package com.zygne.stockanalyzer.domain.interactor.implementation.data.base;

import com.zygne.stockanalyzer.domain.interactor.base.Interactor;

public interface AverageBarVolumeInteractor extends Interactor {

    interface Callback{
        void onAverageBarVolumeCalculated(long avgVol);
    }
}

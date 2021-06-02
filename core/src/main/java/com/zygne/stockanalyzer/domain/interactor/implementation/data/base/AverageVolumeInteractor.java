package com.zygne.stockanalyzer.domain.interactor.implementation.data.base;

import com.zygne.stockanalyzer.domain.interactor.base.Interactor;

public interface AverageVolumeInteractor extends Interactor {

    public interface Callback{
        void onAverageVolumeCalculated(int period, double avgVol);
    }
}

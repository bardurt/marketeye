package com.zygne.stockanalyzer.domain.interactor.implementation.data.base;

import com.zygne.stockanalyzer.domain.interactor.base.Interactor;
import com.zygne.stockanalyzer.domain.model.VolumePriceSum;

import java.util.List;

public interface VolumePriceSumInteractor extends Interactor {

    interface Callback{
        void onVolumePriceSumCreated(List<VolumePriceSum> data);
    }
}

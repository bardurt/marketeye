package com.zygne.stockalyze.domain.interactor.implementation.data.base;

import com.zygne.stockalyze.domain.interactor.base.Interactor;
import com.zygne.stockalyze.domain.model.VolumePriceSum;

import java.util.List;

public interface VolumePriceSumInteractor extends Interactor {

    interface Callback{
        void onVolumePriceSumCreated(List<VolumePriceSum> data);
    }
}

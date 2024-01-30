package com.zygne.data.domain.interactor.implementation.data.base;

import com.zygne.data.domain.model.VolumePriceSum;
import com.zygne.arch.domain.interactor.base.Interactor;

import java.util.List;

public interface VolumePriceSumInteractor extends Interactor {

    interface Callback{
        void onVolumePriceSumCreated(List<VolumePriceSum> data);
    }
}

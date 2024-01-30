package com.zygne.data.domain.interactor.implementation.data.base;

import com.zygne.data.domain.model.VolumePrice;
import com.zygne.arch.domain.interactor.base.Interactor;

import java.util.List;

public interface VolumePriceInteractor extends Interactor {

    interface Callback {
        void onVolumePriceCreated(List<VolumePrice> data);
    }

    public enum PriceStructure{
        H,
        HL,
        OHLC,
        OHLCM,
        SECTION
    }
}

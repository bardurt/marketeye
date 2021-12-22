package com.zygne.stockanalyzer.domain.interactor.implementation.data.base;

import com.zygne.stockanalyzer.domain.interactor.base.Interactor;
import com.zygne.stockanalyzer.domain.model.VolumePrice;

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

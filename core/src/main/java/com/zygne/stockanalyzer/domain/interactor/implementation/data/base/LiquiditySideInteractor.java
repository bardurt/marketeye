package com.zygne.stockanalyzer.domain.interactor.implementation.data.base;

import com.zygne.stockanalyzer.domain.interactor.base.Interactor;
import com.zygne.stockanalyzer.domain.model.LiquiditySide;

import java.util.List;

public interface LiquiditySideInteractor extends Interactor {

    interface Callback{
        void onLiquiditySidesCreated(List<LiquiditySide> data);
    }
}

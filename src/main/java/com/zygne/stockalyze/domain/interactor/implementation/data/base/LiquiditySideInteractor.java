package com.zygne.stockalyze.domain.interactor.implementation.data.base;

import com.zygne.stockalyze.domain.interactor.base.Interactor;
import com.zygne.stockalyze.domain.model.LiquiditySide;

import java.util.List;

public interface LiquiditySideInteractor extends Interactor {

    interface Callback{
        void onLiquiditySidesCreated(List<LiquiditySide> data);
    }
}

package com.zygne.stockalyze.domain.interactor.implementation.data.base;

import com.zygne.stockalyze.domain.interactor.base.Interactor;
import com.zygne.stockalyze.domain.model.LiquiditySide;

import java.util.List;

public interface LiquiditySideFilterInteractor extends Interactor {

    interface Callback{
        void onLiquiditySidesFiltered(List<LiquiditySide> data);
    }

}

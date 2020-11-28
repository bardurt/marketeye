package com.zygne.stockanalyzer.domain.interactor.implementation.data.base;

import com.zygne.stockanalyzer.domain.model.LiquidityLevel;

import java.util.List;

public interface BreakPointInteractor {

    interface Callback{
        void onBreakPointsCalculated(List<LiquidityLevel> data);
    }
}

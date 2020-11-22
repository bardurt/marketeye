package com.zygne.stockalyze.domain.interactor.implementation.data.base;

import com.zygne.stockalyze.domain.model.LiquidityLevel;

import java.util.List;

public interface BreakPointInteractor {

    interface Callback{
        void onBreakPointsCalculated(List<LiquidityLevel> data);
    }
}

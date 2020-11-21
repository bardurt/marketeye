package com.zygne.stockalyze.presentation.presenter.base;

import com.zygne.stockalyze.domain.model.Fundamentals;
import com.zygne.stockalyze.domain.model.LiquidityZone;
import com.zygne.stockalyze.domain.model.enums.TimeFrame;
import com.zygne.stockalyze.presentation.view.BaseView;

import java.util.List;

public interface MainPresenter {
    void getZones(String ticker, double percentile, TimeFrame timeFrame, boolean fundamentalData);

    interface View extends BaseView {
        void onResistanceFound(List<LiquidityZone> zones);
        void onSupportFound(List<LiquidityZone> zones);
        void onPivotFound(List<LiquidityZone> zones);
        void onFundamentalsLoaded(Fundamentals fundamentals);
        void onComplete(String symbol);
        void onTimeFramesPrepared(List<TimeFrame> timeFrames, int defaultSelection);
    }
}

package com.zygne.stockanalyzer.presentation.presenter.base;

import com.zygne.stockanalyzer.domain.model.Fundamentals;
import com.zygne.stockanalyzer.domain.model.LiquidityLevel;
import com.zygne.stockanalyzer.domain.model.enums.TimeFrame;
import com.zygne.stockanalyzer.presentation.view.BaseView;

import java.util.List;

public interface MainPresenter {
    void getZones(String ticker, double percentile, TimeFrame timeFrame, int monthsToFetch, boolean fundamentalData);

    interface View extends BaseView {
        void onResistanceFound(List<LiquidityLevel> zones);
        void onSupportFound(List<LiquidityLevel> zones);
        void onPivotFound(List<LiquidityLevel> zones);
        void onFundamentalsLoaded(Fundamentals fundamentals);
        void onComplete(String symbol, String timeFrame);
        void onTimeFramesPrepared(List<TimeFrame> timeFrames, int defaultSelection);
        void onDataSizePrepared(List<Integer> data, int defaultSelection);
        void onStatusUpdate(String status);
    }
}

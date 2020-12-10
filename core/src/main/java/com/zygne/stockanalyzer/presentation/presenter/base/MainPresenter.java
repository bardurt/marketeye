package com.zygne.stockanalyzer.presentation.presenter.base;

import com.zygne.stockanalyzer.domain.model.DataLength;
import com.zygne.stockanalyzer.domain.model.Fundamentals;
import com.zygne.stockanalyzer.domain.model.LiquidityLevel;
import com.zygne.stockanalyzer.domain.model.PriceGap;
import com.zygne.stockanalyzer.domain.model.enums.TimeInterval;
import com.zygne.stockanalyzer.presentation.view.BaseView;

import java.util.List;

public interface MainPresenter {
    void getZones(String ticker, double percentile, TimeInterval timeInterval, int monthsToFetch, boolean fundamentalData);

    void toggleConnection();

    interface View extends BaseView {
        void onResistanceFound(List<LiquidityLevel> zones);

        void onSupportFound(List<LiquidityLevel> zones);

        void onPivotFound(List<LiquidityLevel> zones);

        void onFundamentalsLoaded(Fundamentals fundamentals);

        void onPriceGapsFound(List<PriceGap> data);

        void onComplete(String symbol, String timeFrame);

        void onTimeFramesPrepared(List<TimeInterval> timeIntervals, int defaultSelection);

        void onDataSizePrepared(List<DataLength> data, int defaultSelection);

        void onStatusUpdate(String status);

        void onConnected();

        void onDisconnected();
    }
}

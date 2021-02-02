package com.zygne.stockanalyzer.presentation.presenter.base;

import com.zygne.stockanalyzer.domain.model.*;
import com.zygne.stockanalyzer.domain.model.enums.TimeInterval;
import com.zygne.stockanalyzer.presentation.view.BaseView;

import java.util.List;

public interface MainPresenter {
    void getZones(String ticker, double percentile, TimeInterval timeInterval, int monthsToFetch, boolean fundamentalData);

    void toggleConnection();

    void findHighVolume();

    interface View extends BaseView {
        void onResistanceFound(List<LiquidityLevel> zones);

        void onSupportFound(List<LiquidityLevel> zones);

        void onPivotFound(List<LiquidityLevel> zones);

        void onFundamentalsLoaded(Fundamentals fundamentals);

        void onPriceGapsFound(List<PriceGap> data);

        void onComplete(String symbol, String timeFrame, String dateRange);

        void onTimeFramesPrepared(List<TimeInterval> timeIntervals, int defaultSelection);

        void onDataSizePrepared(List<DataSize> data, int defaultSelection);

        void onStatusUpdate(String status);

        void onHighVolumeBarFound(List<VolumeBarDetails> data);

        void onConnected();

        void onDisconnected();

        void toggleConnectionSettings(boolean b);

        void prepareView(List<ViewComponent> viewComponents);
    }

    enum ViewComponent{
        VPA,
        PRICE_GAPS,
        INTRA_DAY,
        SCRIPT,
        WICKS
    }
}

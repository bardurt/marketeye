package com.zygne.stockanalyzer.presentation.presenter.base;

import com.zygne.stockanalyzer.domain.api.DataBroker;
import com.zygne.stockanalyzer.domain.model.*;
import com.zygne.stockanalyzer.domain.model.enums.TimeInterval;
import com.zygne.stockanalyzer.presentation.view.BaseView;

import java.util.List;

public interface MainPresenter {
    void createReport(String ticker, double percentile, TimeInterval timeInterval, DataSize dataSize, boolean fundamentalData, boolean cache);

    void setAsset(DataBroker.Asset asset);
    void toggleConnection();

    void findHighVolume();

    interface View extends BaseView {
        void onSupplyCreated(List<LiquidityLevel> filtered, List<LiquidityLevel> raw);
        void onBinnedSupplyCreated(List<LiquidityLevel> zones);
        void onDailyLiquidityGenerated(List<LiquiditySide> data);
        void onDailyBarsCreated(List<Histogram> histograms);
        void onHistogramCreated(List<Histogram> histograms);
        void onWeeklyLiquidityGenerated(List<LiquiditySide> data);
        void onFundamentalsLoaded(Fundamentals fundamentals);
        void onDailyPriceGapsFound(List<PriceGap> data);
        void onIntraDayPriceGapsFound(List<PriceGap> data);
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

    enum AnalysisSettings{
        PERCENTILE,
        CACHE
    }
}

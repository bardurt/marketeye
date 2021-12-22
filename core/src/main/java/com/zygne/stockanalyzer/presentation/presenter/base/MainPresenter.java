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
        void onDailyBarsCreated(List<Histogram> histograms);
        void onHistogramCreated(List<Histogram> histograms);
        void onIntraDayPriceGapsFound(List<PriceGap> data);
        void onComplete(String symbol, String timeFrame, String dateRange);
        void onTimeFramesPrepared(List<TimeInterval> timeIntervals, int defaultSelection);
        void onDataSizePrepared(List<DataSize> data, int defaultSelection);
        void toggleConnectionSettings(boolean b);
        void prepareView(List<ViewComponent> viewComponents);
        void onLatestPriceFetched(double price);
    }

    enum ViewComponent{
        VPA,
        PRICE_GAPS,
        INTRA_DAY,
        SCRIPT,
        WICKS,
        CHART
    }

    enum AnalysisSettings{
        PERCENTILE,
        CACHE
    }
}

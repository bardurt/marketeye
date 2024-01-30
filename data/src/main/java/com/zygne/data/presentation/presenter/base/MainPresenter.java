package com.zygne.data.presentation.presenter.base;

import com.zygne.data.domain.model.*;
import com.zygne.data.domain.model.enums.TimeInterval;
import com.zygne.arch.presentation.view.BaseView;


import java.util.List;

public interface MainPresenter {
    void createReport(String ticker, double percentile, TimeInterval timeInterval, DataSize dataSize);

    interface View extends BaseView {
        void onSupplyCreated(List<LiquidityLevel> filtered, List<LiquidityLevel> raw);
        void onHistogramCreated(List<Histogram> histograms);
        void onComplete(String symbol, String timeFrame, String dateRange);
        void onTimeFramesPrepared(List<TimeInterval> timeIntervals, int defaultSelection);
        void onDataSizePrepared(List<DataSize> data, int defaultSelection);
        void prepareView();
    }
}

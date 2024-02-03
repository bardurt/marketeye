package com.zygne.data.presentation.presenter.base;

import com.zygne.data.domain.model.*;
import com.zygne.arch.presentation.view.BaseView;


import java.util.List;

public interface MainPresenter {
    void createReport(String ticker);

    interface View extends BaseView {
        void onSupplyCreated(List<LiquidityLevel> filtered, List<LiquidityLevel> raw);
        void onHistogramCreated(List<Histogram> histograms);
        void onComplete(String symbol, String dateRange);
        void prepareView();
    }
}

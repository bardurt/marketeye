package com.zygne.data.presentation.presenter;

import com.zygne.arch.presentation.view.BaseView;
import com.zygne.data.domain.model.Histogram;
import com.zygne.data.domain.model.TendencyReport;

import java.util.List;

public interface MainPresenter {
    void createReport(String ticker, int type);
    void adjust(boolean adjustPrices);

    interface View extends BaseView {
        void onComplete(List<Histogram> daily, List<Histogram> weekly, List<Histogram> monthly, TendencyReport tendencyReport, String symbol);
        void prepareView();
    }
}

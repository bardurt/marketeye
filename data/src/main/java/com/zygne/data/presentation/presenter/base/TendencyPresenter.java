package com.zygne.data.presentation.presenter.base;

import com.zygne.data.domain.model.Asset;
import com.zygne.data.domain.model.Histogram;
import com.zygne.data.domain.model.TendencyReport;
import com.zygne.arch.presentation.view.BaseView;

import java.util.List;

public interface TendencyPresenter {

    void createTendency(String symbol);
    void createTendency(List<Histogram> data);

    interface View extends BaseView {
        void onTendencyAssetsPrepared(List<Asset> assets, int defaultSelection);
        void onTendencyReportCreated(TendencyReport tendencyReport);
    }
}

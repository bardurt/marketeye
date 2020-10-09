package com.zygne.stockalyze.presentation.presenter.base;

import com.zygne.stockalyze.domain.model.Histogram;

import java.util.List;

public interface DetailsPresenter {

    void fetchDetails(String ticker, List<Histogram> histogramList);

    public interface View extends BaseView {
        void onDetailsFetched(String details);
    }
}

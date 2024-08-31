package com.zygne.data.presentation.presenter.base;

import com.zygne.arch.presentation.view.BaseView;
import com.zygne.data.domain.model.Histogram;
import com.zygne.data.domain.model.Ratio;

import java.util.List;

public interface RatioPresenter {

    void createRatio(List<Histogram> data);

    public interface View extends BaseView {
        void onRatioCreated(Ratio ratio);
    }
}

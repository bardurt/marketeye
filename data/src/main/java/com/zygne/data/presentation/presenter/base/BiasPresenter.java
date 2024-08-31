package com.zygne.data.presentation.presenter.base;

import com.zygne.arch.presentation.view.BaseView;
import com.zygne.data.domain.model.Bias;

import java.util.List;

public interface BiasPresenter {

    void createBias(String symbol);

    public interface View extends BaseView {
        void onBiasCreated(List<Bias> biasList);
    }
}

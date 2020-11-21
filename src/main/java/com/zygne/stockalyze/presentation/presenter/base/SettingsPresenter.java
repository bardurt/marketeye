package com.zygne.stockalyze.presentation.presenter.base;

import com.zygne.stockalyze.domain.model.Settings;
import com.zygne.stockalyze.presentation.view.BaseView;

public interface SettingsPresenter {

    void start();

    interface View extends BaseView {
        void onSettingsLoaded(Settings settings);
    }
}

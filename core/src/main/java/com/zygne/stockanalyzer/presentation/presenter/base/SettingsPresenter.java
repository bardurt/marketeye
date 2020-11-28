package com.zygne.stockanalyzer.presentation.presenter.base;

import com.zygne.stockanalyzer.domain.model.Settings;
import com.zygne.stockanalyzer.presentation.view.BaseView;

public interface SettingsPresenter {

    void start();

    interface View extends BaseView {
        void onSettingsLoaded(Settings settings);
    }
}

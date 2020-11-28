package com.zygne.stockanalyzer.domain.interactor.implementation;

import com.zygne.stockanalyzer.domain.interactor.base.Interactor;
import com.zygne.stockanalyzer.domain.model.Settings;

public interface SettingsInteractor extends Interactor {

    interface Callback{
        void onSettingsLoaded(Settings settings);
        void onSettingsError(String filename);
    }
}

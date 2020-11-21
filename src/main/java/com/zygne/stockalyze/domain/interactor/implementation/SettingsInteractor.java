package com.zygne.stockalyze.domain.interactor.implementation;

import com.zygne.stockalyze.domain.interactor.base.Interactor;
import com.zygne.stockalyze.domain.model.Settings;

public interface SettingsInteractor extends Interactor {

    interface Callback{
        void onSettingsLoaded(Settings settings);
        void onSettingsError(String filename);
    }
}

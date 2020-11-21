package com.zygne.stockalyze.presentation.presenter.implementation;

import com.zygne.stockalyze.domain.executor.MainThread;
import com.zygne.stockalyze.domain.executor.Executor;
import com.zygne.stockalyze.domain.interactor.implementation.SettingsInteractor;
import com.zygne.stockalyze.domain.interactor.implementation.SettingsInteractorImpl;
import com.zygne.stockalyze.domain.model.Settings;
import com.zygne.stockalyze.presentation.presenter.base.BasePresenter;
import com.zygne.stockalyze.presentation.presenter.base.SettingsPresenter;

public class SettingsPresenterImpl extends BasePresenter implements SettingsPresenter,
        SettingsInteractor.Callback {

    private final View view;

    public SettingsPresenterImpl(Executor executor, MainThread mainThread, View view) {
        super(executor, mainThread);
        this.view = view;
    }

    @Override
    public void start() {
        view.showLoading("Loading Settings");
        new SettingsInteractorImpl(executor, mainThread, this).execute();
    }

    @Override
    public void onSettingsLoaded(Settings settings) {
        view.hideLoading();
        view.onSettingsLoaded(settings);
    }

    @Override
    public void onSettingsError(String filename) {
        view.hideLoading();
        view.showError("Unable to load settings from " + filename);
    }
}

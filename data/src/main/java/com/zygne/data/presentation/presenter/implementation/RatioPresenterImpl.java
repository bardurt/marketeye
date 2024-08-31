package com.zygne.data.presentation.presenter.implementation;

import com.zygne.arch.domain.executor.Executor;
import com.zygne.arch.domain.executor.MainThread;
import com.zygne.arch.presentation.presenter.base.BasePresenter;
import com.zygne.data.domain.interactor.implementation.data.RatioInteractorImpl;
import com.zygne.data.domain.interactor.implementation.data.base.RatioInteractor;
import com.zygne.data.domain.model.Histogram;
import com.zygne.data.domain.model.Ratio;
import com.zygne.data.presentation.presenter.base.RatioPresenter;

import java.util.List;

public class RatioPresenterImpl extends BasePresenter implements RatioPresenter,
        RatioInteractor.Callback {

    private final View view;

    public RatioPresenterImpl(Executor executor, MainThread mainThread, View view) {
        super(executor, mainThread);
        this.view = view;
    }

    @Override
    public void createRatio(List<Histogram> data) {
        view.showLoading("Creating ratio");
        new RatioInteractorImpl(executor, mainThread, this, data).execute();
    }

    @Override
    public void onRatioCreated(Ratio ratio) {
        view.hideLoading();
        this.view.onRatioCreated(ratio);
    }
}

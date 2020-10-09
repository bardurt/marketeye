package com.zygne.stockalyze.presentation.presenter.base;

public interface BaseView {

    void showLoading(String message);
    void hideLoading();
    void showError(String message);
}

package com.zygne.stockalyze.presentation.view;

public interface BaseView {

    void showLoading(String message);
    void hideLoading();
    void showError(String message);
}

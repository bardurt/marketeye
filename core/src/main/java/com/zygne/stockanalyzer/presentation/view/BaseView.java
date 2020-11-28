package com.zygne.stockanalyzer.presentation.view;

public interface BaseView {

    void showLoading(String message);
    void hideLoading();
    void showError(String message);
}

package com.zygne.arch.presentation.view;

public interface BaseView {

    void showLoading(String message);
    void hideLoading();
    void showError(String message);
}

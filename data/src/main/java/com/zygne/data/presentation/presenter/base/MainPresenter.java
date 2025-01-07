package com.zygne.data.presentation.presenter.base;

import com.zygne.data.domain.model.*;
import com.zygne.arch.presentation.view.BaseView;


import java.util.List;

public interface MainPresenter {
    void createReport(String ticker);

    interface View extends BaseView {
        void onComplete(List<Histogram> daily, List<Histogram> weekly, TendencyReport tendencyReport, String symbol);
        void prepareView();
    }
}

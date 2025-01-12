package com.zygne.data.presentation.presenter;

import com.zygne.data.domain.model.*;
import com.zygne.arch.presentation.view.BaseView;


import java.util.List;

public interface MainPresenter {
    void createReport(String ticker, int type);

    interface View extends BaseView {
        void onComplete(List<Histogram> daily, List<Histogram> weekly, List<Histogram> monthly,TendencyReport tendencyReport, String symbol);
        void prepareView();
    }
}

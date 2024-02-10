package com.zygne.data.presentation.presenter.base;

import com.zygne.data.domain.model.CotData;

import java.util.List;

public interface CotPresenter {

    void createReport(String symbol);

    interface View{
        void onCotDataReady(List<CotData> cotData);
    }

}

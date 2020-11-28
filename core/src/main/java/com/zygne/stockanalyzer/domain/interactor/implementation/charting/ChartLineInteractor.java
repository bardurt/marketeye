package com.zygne.stockanalyzer.domain.interactor.implementation.charting;

import com.zygne.stockanalyzer.domain.interactor.base.Interactor;
import com.zygne.stockanalyzer.domain.model.graphics.ChartObject;

import java.util.List;

public interface ChartLineInteractor extends Interactor {

    interface Callback{
        void onChartLineCreated(List<ChartObject> lines);
    }
}

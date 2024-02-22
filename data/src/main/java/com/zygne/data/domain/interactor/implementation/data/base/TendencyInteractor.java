package com.zygne.data.domain.interactor.implementation.data.base;

import com.zygne.data.domain.model.TendencyReport;
import com.zygne.arch.domain.interactor.base.Interactor;

public interface TendencyInteractor extends Interactor {

    interface Callback{
        void onTendencyReportCreated(TendencyReport tendencyReport);
    }
}

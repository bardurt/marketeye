package com.zygne.data.domain.interactor.implementation.data;

import com.zygne.data.domain.model.TendencyReport;
import com.zygne.arch.domain.interactor.Interactor;

public interface TendencyInteractor extends Interactor {

    interface Callback{
        void onTendencyReportCreated(TendencyReport tendencyReport);
    }
}

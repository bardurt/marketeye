package com.zygne.stockanalyzer.domain.interactor.implementation.data.base;

import com.zygne.stockanalyzer.domain.interactor.base.Interactor;
import com.zygne.stockanalyzer.domain.model.GapHistory;

public interface GapHistoryInteractor extends Interactor {

    interface Callback {
        void onGapHistoryCompleted(GapHistory gapHistory);
    }
}

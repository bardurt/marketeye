package com.zygne.data.domain.interactor.implementation.data.base;


import com.zygne.data.domain.model.GapHistory;
import com.zygne.arch.domain.interactor.base.Interactor;

public interface GapHistoryInteractor extends Interactor {

    interface Callback {
        void onGapHistoryCompleted(GapHistory gapHistory);
    }
}

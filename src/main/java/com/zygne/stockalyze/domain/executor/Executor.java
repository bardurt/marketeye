package com.zygne.stockalyze.domain.executor;

import com.zygne.stockalyze.domain.interactor.base.BaseInteractor;
import com.zygne.stockalyze.domain.interactor.base.Interactor;

public interface Executor {

    void execute(BaseInteractor interactor);
}

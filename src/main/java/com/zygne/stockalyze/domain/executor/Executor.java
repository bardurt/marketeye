package com.zygne.stockalyze.domain.executor;

import com.zygne.stockalyze.domain.interactor.base.BaseInteractor;

public interface Executor {

    void execute(BaseInteractor interactor);

    void stop();
}

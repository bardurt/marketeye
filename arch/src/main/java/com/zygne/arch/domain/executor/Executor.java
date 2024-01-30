package com.zygne.arch.domain.executor;


import com.zygne.arch.domain.interactor.base.BaseInteractor;

public interface Executor {

    void execute(BaseInteractor interactor);

    void stop();
}

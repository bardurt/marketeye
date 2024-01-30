package domain.executor;


import domain.interactor.base.BaseInteractor;

public interface Executor {

    void execute(BaseInteractor interactor);

    void stop();
}

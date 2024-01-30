package presentation.presenter.base;

import domain.executor.Executor;
import domain.executor.MainThread;

public abstract class BasePresenter {

    protected final Executor executor;
    protected MainThread mainThread;

    public BasePresenter(Executor executor, MainThread mainThread) {
        this.executor = executor;
        this.mainThread = mainThread;
    }
}

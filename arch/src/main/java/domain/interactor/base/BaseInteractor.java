package domain.interactor.base;


import domain.executor.Executor;
import domain.executor.MainThread;

public abstract class BaseInteractor implements Interactor {

    protected final Executor executor;
    protected final MainThread mainThread;

    public BaseInteractor(Executor executor, MainThread mainThread) {
        this.executor = executor;
        this.mainThread = mainThread;
    }

    protected boolean completed = false;

    public abstract void run();

    @Override
    public void execute() {
        executor.execute(this);
    }
}

package com.mz.twitterpuller.interactor;

import com.mz.twitterpuller.util.executor.PostExecutionThread;
import com.mz.twitterpuller.util.executor.ThreadExecutor;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import java.util.concurrent.Executor;

/**
 * Abstract class for an execution unit for different case
 * Each implementation will return the result using @{@link DisposableObserver} executing all work
 * in a background thread and will post the result in the UI thread.
 */
public abstract class Interactor<T, P> {

  private final Executor executor;
  private final PostExecutionThread postExecutionThread;
  private final CompositeDisposable disposables;

  public Interactor(ThreadExecutor executor, PostExecutionThread postExecutionThread) {
    this.executor = executor;
    this.postExecutionThread = postExecutionThread;
    disposables = new CompositeDisposable();
  }

  abstract Observable<T> buildInteractor(P params);

  /**
   * Execute the current interactor
   *
   * @param observer @{@link DisposableObserver}
   * @param params use to build/execute this interactor
   */
  public void execute(DisposableObserver<T> observer, P params) {
    final Observable<T> observable = buildInteractor(params).subscribeOn(Schedulers.from(executor))
        .observeOn(postExecutionThread.getScheduler());
    addDisposable(observable.subscribeWith(observer));
  }

  public void dispose() {
    if (!disposables.isDisposed()) {
      disposables.dispose();
    }
  }

  private void addDisposable(Disposable disposable) {
    disposables.add(disposable);
  }
}

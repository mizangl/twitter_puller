package com.mz.twitterpuller.interactor;

import io.reactivex.observers.DisposableObserver;

/**
 * Default @{@link DisposableObserver} base class
 * @param <T> what ever you want to handle
 */
public class DefaultObserver<T> extends DisposableObserver<T> {
  @Override public void onNext(T value) {
    //empty by default
  }

  @Override public void onError(Throwable e) {
    //empty by default
  }

  @Override public void onComplete() {
    //empty by default
  }
}

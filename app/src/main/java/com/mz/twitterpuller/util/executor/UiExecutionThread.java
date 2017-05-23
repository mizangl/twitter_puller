package com.mz.twitterpuller.util.executor;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * UIThread implementation based on a @{@link Scheduler} which will execute jobs on the Android UI
 * thread
 */
@Singleton public class UiExecutionThread implements PostExecutionThread {

  @Inject UiExecutionThread() {
  }

  @Override public Scheduler getScheduler() {
    return AndroidSchedulers.mainThread();
  }
}

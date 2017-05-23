package com.mz.twitterpuller.util.executor;

import io.reactivex.Scheduler;

/**
 * Abstraction interface create to change the execution context
 */
public interface PostExecutionThread {

  Scheduler getScheduler();
}

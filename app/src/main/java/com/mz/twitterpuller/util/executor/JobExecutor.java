package com.mz.twitterpuller.util.executor;

import android.support.annotation.NonNull;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Decorated @{@link ThreadExecutor}
 */
@Singleton public class JobExecutor implements ThreadExecutor {

  private static final int CORE_POOL_SIZE = 2;
  private static final int MAXIMUM_POOL_SIZE = 4;
  private static final int KEEP_ALIVE_TIME = 15;

  private final ThreadPoolExecutor threadPoolExecutor;

  @Inject JobExecutor() {
    final BlockingQueue<Runnable> blockingQueue = new LinkedBlockingQueue<>();
    threadPoolExecutor =
        new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS,
            blockingQueue, new CustomThreadFactory());
  }

  @Override public void execute(@NonNull Runnable command) {
    threadPoolExecutor.execute(command);
  }

  private static class CustomThreadFactory implements ThreadFactory {

    private int internal = 0;

    @Override public Thread newThread(@NonNull Runnable r) {
      return new Thread(r, "job_" + internal++);
    }
  }
}

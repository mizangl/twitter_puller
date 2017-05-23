package com.mz.twitterpuller.internal;

import android.content.Context;
import com.mz.twitterpuller.util.executor.JobExecutor;
import com.mz.twitterpuller.util.executor.PostExecutionThread;
import com.mz.twitterpuller.util.executor.ThreadExecutor;
import com.mz.twitterpuller.util.executor.UiExecutionThread;
import dagger.Module;
import dagger.Provides;

@Module public class ApplicationModule {

  private final Context context;

  public ApplicationModule(Context context) {
    this.context = context;
  }

  @Provides public Context provideContext() {
    return context;
  }

  @Provides ThreadExecutor provideThreadExecutor(JobExecutor jobExecutor) {
    return jobExecutor;
  }

  @Provides PostExecutionThread providePostExecutionThread(UiExecutionThread uiExecutionThread) {
    return uiExecutionThread;
  }
}

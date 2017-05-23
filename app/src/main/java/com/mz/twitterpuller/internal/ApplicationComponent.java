package com.mz.twitterpuller.internal;

import com.mz.twitterpuller.data.RepositoryModule;
import com.mz.twitterpuller.data.TwitterRepository;
import com.mz.twitterpuller.util.executor.PostExecutionThread;
import com.mz.twitterpuller.util.executor.ThreadExecutor;
import dagger.Component;
import javax.inject.Singleton;

@Singleton @Component(modules = { ApplicationModule.class, RepositoryModule.class })
public interface ApplicationComponent {

  ThreadExecutor getThreadExecutor();

  PostExecutionThread getPostExecutionThread();

  TwitterRepository getTwitterRepository();
}

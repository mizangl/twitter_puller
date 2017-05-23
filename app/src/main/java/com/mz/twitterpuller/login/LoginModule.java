package com.mz.twitterpuller.login;

import com.mz.twitterpuller.data.TwitterRepository;
import com.mz.twitterpuller.interactor.GetSessionInteractor;
import com.mz.twitterpuller.interactor.Interactor;
import com.mz.twitterpuller.util.executor.PostExecutionThread;
import com.mz.twitterpuller.util.executor.ThreadExecutor;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;

@Module public class LoginModule {

  private final LoginContract.View view;

  public LoginModule(LoginContract.View view) {
    this.view = view;
  }

  @Provides @Named("session") Interactor provideSessionInteractor(ThreadExecutor threadExecutor,
      PostExecutionThread postExecutionThread, TwitterRepository repository) {
    return new GetSessionInteractor(threadExecutor, postExecutionThread, repository);
  }

  @Provides LoginContract.View provideView() {
    return view;
  }
}

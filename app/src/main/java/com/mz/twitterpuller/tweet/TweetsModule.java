package com.mz.twitterpuller.tweet;

import com.mz.twitterpuller.data.TwitterRepository;
import com.mz.twitterpuller.interactor.GetTweets;
import com.mz.twitterpuller.interactor.Interactor;
import com.mz.twitterpuller.util.executor.PostExecutionThread;
import com.mz.twitterpuller.util.executor.ThreadExecutor;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;

@Module public class TweetsModule {

  private final TweetsContract.View view;

  public TweetsModule(TweetsContract.View view) {
    this.view = view;
  }

  @Provides TweetsContract.View provideView() {
    return view;
  }

  @Provides @Named("tweets") Interactor provideGetTweetsInteractor(ThreadExecutor threadExecutor,
      PostExecutionThread postExecutionThread, TwitterRepository repository) {
    return new GetTweets(threadExecutor, postExecutionThread, repository);
  }
}

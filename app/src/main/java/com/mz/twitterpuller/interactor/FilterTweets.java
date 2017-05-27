package com.mz.twitterpuller.interactor;

import com.mz.twitterpuller.data.TwitterRepository;
import com.mz.twitterpuller.data.model.TweetModel;
import com.mz.twitterpuller.util.executor.PostExecutionThread;
import com.mz.twitterpuller.util.executor.ThreadExecutor;
import io.reactivex.Observable;
import java.util.List;

public class FilterTweets extends Interactor<List<TweetModel>, CharSequence> {

  private final TwitterRepository repository;

  public FilterTweets(ThreadExecutor executor, PostExecutionThread postExecutionThread,
      TwitterRepository repository) {
    super(executor, postExecutionThread);
    this.repository = repository;
  }

  @Override Observable<List<TweetModel>> buildInteractor(CharSequence params) {
    return repository.filterBy(params);
  }
}

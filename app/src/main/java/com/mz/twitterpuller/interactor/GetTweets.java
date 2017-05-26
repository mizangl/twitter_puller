package com.mz.twitterpuller.interactor;

import com.mz.twitterpuller.data.TwitterRepository;
import com.mz.twitterpuller.data.model.TweetModel;
import com.mz.twitterpuller.util.executor.PostExecutionThread;
import com.mz.twitterpuller.util.executor.ThreadExecutor;
import io.reactivex.Observable;
import java.util.Map;

public class GetTweets extends Interactor<TweetModel, Map<String, Number>> {

  public static final String PARAM_COUNT = "count";
  public static final String PARAM_SINCE = "since";
  public static final String PARAM_MAX = "max";
  private final TwitterRepository repository;

  public GetTweets(ThreadExecutor executor, PostExecutionThread postExecutionThread,
      TwitterRepository repository) {
    super(executor, postExecutionThread);
    this.repository = repository;
  }

  @Override Observable<TweetModel> buildInteractor(Map<String, Number> params) {

    final Integer count = (Integer) params.get(PARAM_COUNT);
    final Long since = (Long) params.get(PARAM_SINCE);
    final Long max = (Long) params.get(PARAM_MAX);
    return repository.pullTweets(count, since, max);
  }
}

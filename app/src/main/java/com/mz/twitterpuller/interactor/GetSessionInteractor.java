package com.mz.twitterpuller.interactor;

import android.app.Activity;
import com.mz.twitterpuller.data.TwitterRepository;
import com.mz.twitterpuller.util.executor.PostExecutionThread;
import com.mz.twitterpuller.util.executor.ThreadExecutor;
import io.reactivex.Observable;

public class GetSessionInteractor extends Interactor<Boolean, Activity> {

  private final TwitterRepository twitterRepository;

  public GetSessionInteractor(ThreadExecutor executor, PostExecutionThread postExecutionThread,
      TwitterRepository twitterRepository) {
    super(executor, postExecutionThread);
    this.twitterRepository = twitterRepository;
  }

  @Override Observable<Boolean> buildInteractor(Activity params) {
    return twitterRepository.doLogin(params);
  }
}

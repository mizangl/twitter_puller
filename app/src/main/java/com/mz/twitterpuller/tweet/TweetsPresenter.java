package com.mz.twitterpuller.tweet;

import android.os.Bundle;
import com.mz.twitterpuller.data.model.TweetModel;
import com.mz.twitterpuller.interactor.DefaultObserver;
import com.mz.twitterpuller.interactor.GetTweets;
import com.mz.twitterpuller.interactor.Interactor;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;

import static com.mz.twitterpuller.tweet.TweetsContract.View.EXTRA_MAX;
import static com.mz.twitterpuller.tweet.TweetsContract.View.EXTRA_PROGRESS;
import static com.mz.twitterpuller.tweet.TweetsContract.View.EXTRA_SINCE;

final class TweetsPresenter implements TweetsContract.Presenter {

  static final int COUNT = 50;
  private final Interactor<TweetModel, Map<String, Number>> getTweetsInteractor;
  private final TweetsContract.View tweetsView;
  private long since = -1;
  private long max = -1;
  private boolean isInProgress;

  @Inject TweetsPresenter(@Named("tweets") Interactor getTweetsInteractor,
      TweetsContract.View tweetsView) {
    this.getTweetsInteractor = getTweetsInteractor;
    this.tweetsView = tweetsView;
  }

  @Inject void setupView() {
    tweetsView.setPresenter(this);
  }

  @Override public void start() {
    final Map<String, Number> params = new HashMap<>();
    params.put(GetTweets.PARAM_COUNT, COUNT);
    getTweetsInteractor.execute(new TweetsObserver(), params);
  }

  @Override public void pullNews() {
    final Map<String, Number> params = new HashMap<>();
    params.put(GetTweets.PARAM_COUNT, COUNT);
    params.put(GetTweets.PARAM_SINCE, since);

    getTweetsInteractor.execute(new TweetsObserver(), params);
  }

  @Override public void pullOlder() {
    if (isInProgress) return;

    isInProgress = true;
    final Map<String, Number> params = new HashMap<>();
    params.put(GetTweets.PARAM_COUNT, COUNT);
    params.put(GetTweets.PARAM_MAX, max);

    getTweetsInteractor.execute(new TweetsObserver(), params);
  }

  @Override public void saveInstance(Bundle bundle) {
    bundle.putLong(EXTRA_SINCE, since);
    bundle.putLong(EXTRA_MAX, max);
    bundle.putBoolean(EXTRA_PROGRESS, isInProgress);
  }

  @Override public void restoreInstance(Bundle savedInstanceState) {
    if (savedInstanceState == null) return;
    since = savedInstanceState.getLong(EXTRA_SINCE, -1);
    max = savedInstanceState.getLong(EXTRA_MAX, -1);
    isInProgress = savedInstanceState.getBoolean(EXTRA_PROGRESS);
  }

  @Override public void updateSinceAndMax(long first, long last) {
    since = first;
    max = last;
  }

  private class TweetsObserver extends DefaultObserver<TweetModel> {
    @Override public void onNext(TweetModel value) {
      if (isInProgress) {
        isInProgress = false;
        tweetsView.removeProgress();
      }
      tweetsView.bind(value);
    }

    @Override public void onError(Throwable e) {
      tweetsView.setProgressIndicator(false);
    }

    @Override public void onComplete() {
      tweetsView.setProgressIndicator(false);
      tweetsView.updateSinceAndMax();
    }

    @Override protected void onStart() {
      if (isInProgress) {
        tweetsView.addProgress();
      } else {
        tweetsView.setProgressIndicator(true);
      }
    }
  }
}

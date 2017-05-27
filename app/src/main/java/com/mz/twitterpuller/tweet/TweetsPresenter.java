package com.mz.twitterpuller.tweet;

import android.os.Bundle;
import android.text.TextUtils;
import com.mz.twitterpuller.data.model.TweetModel;
import com.mz.twitterpuller.interactor.DefaultObserver;
import com.mz.twitterpuller.interactor.GetTweets;
import com.mz.twitterpuller.interactor.Interactor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;
import timber.log.Timber;

import static com.mz.twitterpuller.tweet.TweetsContract.View.EXTRA_MAX;
import static com.mz.twitterpuller.tweet.TweetsContract.View.EXTRA_PROGRESS;
import static com.mz.twitterpuller.tweet.TweetsContract.View.EXTRA_SINCE;

final class TweetsPresenter implements TweetsContract.Presenter {

  static final int COUNT = 50;

  private final Interactor<List<TweetModel>, Map<String, Number>> getTweetsInteractor;
  private final Interactor<List<TweetModel>, CharSequence> getFilterTweetsInteractor;

  private final TweetsContract.View tweetsView;
  private long since = -1;
  private long max = -1;
  private boolean isInProgress;
  private boolean isShowingFiltered;

  @Inject TweetsPresenter(@Named("tweets") Interactor getTweetsInteractor,
      @Named("filter") Interactor getFilterTweetsInteractor, TweetsContract.View tweetsView) {
    this.getTweetsInteractor = getTweetsInteractor;
    this.getFilterTweetsInteractor = getFilterTweetsInteractor;
    this.tweetsView = tweetsView;

    Timber.tag(TweetsPresenter.class.getSimpleName());
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
    if(isShowingFiltered) return;

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

  @Override public void search(CharSequence newText) {
    if (TextUtils.isEmpty(newText)){
      tweetsView.removeFiltered();
      isShowingFiltered = false;
      return;
    }
    isShowingFiltered = true;
    getFilterTweetsInteractor.execute(new FilterObserver(), newText);
  }

  private void updateSinceAndMax(List<TweetModel> models) {
    if (models == null || models.isEmpty()) return;
    since = models.get(0).id;
    max = models.get(models.size() - 1).id;
  }

  private class TweetsObserver extends DefaultObserver<List<TweetModel>> {
    @Override public void onNext(List<TweetModel> values) {
      if (isInProgress) {
        isInProgress = false;
        tweetsView.removeProgress();
      }

      updateSinceAndMax(values);
      tweetsView.bind(values);
    }

    @Override public void onError(Throwable e) {
      if (isInProgress) {
        isInProgress = false;
        tweetsView.removeProgress();
      }
      tweetsView.setProgressIndicator(false);

      Timber.e("onError: ", e);
    }

    @Override public void onComplete() {
      tweetsView.setProgressIndicator(false);
    }

    @Override protected void onStart() {
      if (isInProgress) {
        tweetsView.addProgress();
      } else {
        tweetsView.setProgressIndicator(true);
      }
    }
  }

  private class FilterObserver extends DefaultObserver<List<TweetModel>> {
    @Override public void onNext(List<TweetModel> values) {
      tweetsView.bindFiltered(values);
    }

    @Override public void onError(Throwable e) {
    }

    @Override public void onComplete() {

    }

    @Override protected void onStart() {

    }
  }
}

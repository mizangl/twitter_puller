package com.mz.twitterpuller.data;

import android.app.Activity;
import com.mz.twitterpuller.data.model.TweetModel;
import com.mz.twitterpuller.data.source.DataSource;
import io.reactivex.Observable;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton public class TwitterRepository implements DataSource {

  private final DataSource remoteDataSource;

  @Inject TwitterRepository(DataSource remoteDataSource) {
    this.remoteDataSource = remoteDataSource;
  }

  @Override public Observable<Boolean> doLogin(final Activity activity) {
    return remoteDataSource.doLogin(activity);
  }

  @Override public Observable<TweetModel> pullTweets(final Integer count, final Long since, final Long max) {
    return remoteDataSource.pullTweets(count, since, max);
  }
}

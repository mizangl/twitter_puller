package com.mz.twitterpuller.data;

import android.app.Activity;
import com.mz.twitterpuller.data.model.TweetModel;
import com.mz.twitterpuller.data.source.DataSource;
import com.mz.twitterpuller.data.source.Local;
import com.mz.twitterpuller.data.source.Remote;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton public class TwitterRepository implements DataSource {

  private final DataSource remoteDataSource;
  private final DataSource localDataSource;

  @Inject TwitterRepository(@Remote DataSource remoteDataSource,
      @Local DataSource localDataSource) {
    this.remoteDataSource = remoteDataSource;
    this.localDataSource = localDataSource;
  }

  @Override public Observable<Boolean> doLogin(final Activity activity) {
    return remoteDataSource.doLogin(activity);
  }

  @Override public Observable<List<TweetModel>> pullTweets(final Integer count, final Long since,
      final Long max) {

    final Observable<List<TweetModel>> localObservable =
        localDataSource.pullTweets(count, since, max).filter(new Predicate<List<TweetModel>>() {
          @Override public boolean test(List<TweetModel> models) throws Exception {
            return (since == null && max == null);
          }
        });

    final Observable<List<TweetModel>> remoteObservable =
        remoteDataSource.pullTweets(count, since, max).doOnNext(new Consumer<List<TweetModel>>() {
          @Override public void accept(List<TweetModel> models) throws Exception {
            savedLocally(models);
          }
        });

    return Observable.concat(localObservable, remoteObservable);
  }

  @Override public void savedLocally(List<TweetModel> models) {
    localDataSource.savedLocally(models);
  }
}

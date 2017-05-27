package com.mz.twitterpuller.data.source.local;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.VisibleForTesting;
import com.mz.twitterpuller.data.model.TweetModel;
import com.mz.twitterpuller.data.model.mapper.TweetModelMapper;
import com.mz.twitterpuller.data.source.DataSource;
import com.mz.twitterpuller.data.source.local.TweetsDbHelper.Tables;
import com.twitter.sdk.android.core.TwitterCore;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import java.util.List;

public class LocalTwitterDataSource implements DataSource {

  private final TweetsDbHelper helper;
  private final TweetModelMapper mapper;

  public LocalTwitterDataSource(Context context, TweetModelMapper mapper) {
    helper = new TweetsDbHelper(context);
    this.mapper = mapper;
  }

  @Override public Observable<Boolean> doLogin(Activity activity) {
    final boolean isLogged =
        TwitterCore.getInstance().getSessionManager().getActiveSession() != null;
    return Observable.just(isLogged);
  }

  @Override
  public Observable<List<TweetModel>> pullTweets(final Integer count, Long since, final Long max) {
    return Observable.create(new ObservableOnSubscribe<List<TweetModel>>() {
      @Override public void subscribe(ObservableEmitter<List<TweetModel>> emitter)
          throws Exception {
        List<TweetModel> tweets = helper.getTweets(mapper);

        if (!tweets.isEmpty()) emitter.onNext(tweets);

        emitter.onComplete();
      }
    });
  }

  @Override public void savedLocally(final List<TweetModel> models) {
    helper.save(models, mapper);
  }

  public void cleanLocal() {

    SQLiteDatabase database = helper.getWritableDatabase();

    database.delete(Tables.TWEET_TABLE, null, null);

    database.close();
  }

  @Override public Observable<List<TweetModel>> filterBy(final CharSequence params) {
    return Observable.create(new ObservableOnSubscribe<List<TweetModel>>() {
      @Override public void subscribe(ObservableEmitter<List<TweetModel>> emitter)
          throws Exception {
        List<TweetModel> search = helper.search(params, mapper);
        emitter.onNext(search);
        emitter.onComplete();
      }
    });
  }

  @VisibleForTesting public List<TweetModel> getTweetsFromDB() {
    List<TweetModel> models = helper.getTweets(mapper);
    return models;
  }
}

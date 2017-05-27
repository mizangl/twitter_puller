package com.mz.twitterpuller.data.source.remote;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.mz.twitterpuller.data.exception.NoInternetConnectionException;
import com.mz.twitterpuller.data.exception.RateLimitExceededException;
import com.mz.twitterpuller.data.exception.UnAuthorizedException;
import com.mz.twitterpuller.data.model.TweetModel;
import com.mz.twitterpuller.data.model.mapper.TweetModelMapper;
import com.mz.twitterpuller.data.source.DataSource;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.Tweet;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import java.util.List;
import javax.inject.Singleton;
import retrofit2.Call;
import retrofit2.Response;

@Singleton public class RemoteTwitterDataSource implements DataSource {

  private static final boolean TRIM_USER = false;
  private static final boolean EXCLUDE_REPLIES = true;
  private static final boolean CONTRIBUTE_DETAILS = true;
  private static final boolean INCLUDE_ENTITIES = false;

  private final Context context;
  private final TweetModelMapper tweetModelMapper;

  public RemoteTwitterDataSource(Context context, TweetModelMapper tweetModelMapper) {
    this.context = context;
    this.tweetModelMapper = tweetModelMapper;
  }

  @Override public Observable<Boolean> doLogin(final Activity activity) {
    return Observable.create(new ObservableOnSubscribe<Boolean>() {
      @Override public void subscribe(final ObservableEmitter<Boolean> emitter) throws Exception {
        if (isThereInternetConnection()) {
          TwitterCore.getInstance().logIn(activity, new Callback<TwitterSession>() {
            @Override public void success(Result<TwitterSession> result) {
              if (result != null && result.data != null) {
                emitter.onNext(true);
              } else {
                emitter.onNext(false);
              }
              emitter.onComplete();
            }

            @Override public void failure(TwitterException exception) {
              emitter.onError(exception);
            }
          });
        } else {
          emitter.onError(new NoInternetConnectionException());
        }
      }
    });
  }

  @Override public Observable<List<TweetModel>> pullTweets(final Integer count, final Long since,
      final Long max) {
    return Observable.create(new ObservableOnSubscribe<List<TweetModel>>() {
      @Override public void subscribe(ObservableEmitter<List<TweetModel>> emitter)
          throws Exception {
        if (isThereInternetConnection()) {

          Call<List<Tweet>> call = TwitterCore.getInstance()
              .getApiClient()
              .getStatusesService()
              .homeTimeline(count, since, max, TRIM_USER, EXCLUDE_REPLIES, CONTRIBUTE_DETAILS,
                  INCLUDE_ENTITIES);

          final Response<List<Tweet>> response = call.execute();

          if (response.isSuccessful()) {
            List<TweetModel> models = tweetModelMapper.transform(response.body());
            emitter.onNext(models);
            emitter.onComplete();
          } else {
            if (response.code() == 401) {
              emitter.onError(new UnAuthorizedException());
            }
            if (response.code() == 429) {
              emitter.onError(new RateLimitExceededException());
            } else {
              emitter.onError(new UnknownError("Error retrieving tweets"));
            }
          }
        } else {
          emitter.onError(new NoInternetConnectionException());
        }
      }
    });
  }

  @Override public void savedLocally(List<TweetModel> models) {
    throw new UnsupportedOperationException();
  }

  @Override public Observable<List<TweetModel>> filterBy(CharSequence params) {
    throw new UnsupportedOperationException();
  }

  private boolean isThereInternetConnection() {
    boolean isConnected;

    ConnectivityManager connectivityManager =
        (ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
    isConnected = (networkInfo != null && networkInfo.isConnectedOrConnecting());

    return isConnected;
  }
}

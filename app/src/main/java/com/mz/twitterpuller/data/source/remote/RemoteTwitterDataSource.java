package com.mz.twitterpuller.data.source.remote;

import android.app.Activity;
import android.content.Context;
import com.mz.twitterpuller.data.source.DataSource;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import javax.inject.Singleton;

@Singleton public class RemoteTwitterDataSource implements DataSource {

  public RemoteTwitterDataSource(Context context) {
  }

  @Override public Observable<Boolean> doLogin(final Activity activity) {
    return Observable.create(new ObservableOnSubscribe<Boolean>() {
      @Override public void subscribe(final ObservableEmitter<Boolean> emitter) throws Exception {
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
      }
    });
  }
}

package com.mz.twitterpuller.data.source;

import android.app.Activity;
import com.mz.twitterpuller.data.model.TweetModel;
import io.reactivex.Observable;

public interface DataSource {

  Observable<Boolean> doLogin(Activity activity);

  Observable<TweetModel> pullTweets(final Integer count, final Long since, final Long max);
}

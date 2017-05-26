package com.mz.twitterpuller.data.source.remote;

import android.app.Activity;
import android.content.Context;
import com.google.gson.Gson;
import com.mz.twitterpuller.data.model.TweetModel;
import com.mz.twitterpuller.data.model.mapper.TweetModelMapper;
import com.mz.twitterpuller.data.source.DataSource;
import com.twitter.sdk.android.core.models.Tweet;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import java.util.Arrays;
import java.util.List;

public class FakeRemoteDataSource implements DataSource {

  private final Context context;
  private final TweetModelMapper mapper;
  private Gson gson = new Gson();

  public FakeRemoteDataSource(Context context, TweetModelMapper mapper) {
    this.context = context;
    this.mapper = mapper;
  }

  @Override public Observable<Boolean> doLogin(Activity activity) {
    return Observable.just(true);
  }

  @Override public Observable<List<TweetModel>> pullTweets(Integer count, Long since, Long max) {
    return Observable.create(new ObservableOnSubscribe<List<TweetModel>>() {
      @Override public void subscribe(ObservableEmitter<List<TweetModel>> emitter) throws Exception {
        List<Tweet> tweetsList = createTweetsList();
        emitter.onNext(mapper.transform(tweetsList));
        emitter.onComplete();
      }
    });
  }

  @Override public void savedLocally(List<TweetModel> models) {
    throw new UnsupportedOperationException();
  }

  private List<Tweet> createTweetsList() {
    String fromFile = Json.readJsonFromFile(context, "json/tweets.json");
    Tweet[] tweets = gson.fromJson(fromFile, Tweet[].class);

    return Arrays.asList(tweets);
  }
}

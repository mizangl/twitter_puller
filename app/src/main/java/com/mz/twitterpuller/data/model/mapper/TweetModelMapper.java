package com.mz.twitterpuller.data.model.mapper;

import com.mz.twitterpuller.data.model.TweetModel;
import com.twitter.sdk.android.core.models.Tweet;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton public class TweetModelMapper {

  @Inject public TweetModelMapper() {
  }

  public TweetModel transform(Tweet tweet){
    TweetModel model = new TweetModel();
    model.id = tweet.id;
    model.content = tweet.text;
    model.username = tweet.user.screenName;
    model.profile = tweet.user.profileImageUrl;

    return model;
  }
}

package com.mz.twitterpuller.data.model.mapper;

import android.database.Cursor;
import com.mz.twitterpuller.data.model.TweetModel;
import com.twitter.sdk.android.core.models.Tweet;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

import static com.mz.twitterpuller.data.source.local.TweetEntry.COLUMN_CONTENT;
import static com.mz.twitterpuller.data.source.local.TweetEntry.COLUMN_CREATED_AT;
import static com.mz.twitterpuller.data.source.local.TweetEntry.COLUMN_ID;
import static com.mz.twitterpuller.data.source.local.TweetEntry.COLUMN_PROFILE;
import static com.mz.twitterpuller.data.source.local.TweetEntry.COLUMN_USERNAME;

@Singleton public class TweetModelMapper {

  @Inject public TweetModelMapper() {
  }

  public List<TweetModel> transform(List<Tweet> tweets) {

    List<TweetModel> models = new ArrayList<>();
    TweetModel model = null;
    for (Tweet tweet : tweets) {
      model = transform(tweet);
      models.add(model);
    }

    return models;
  }

  public TweetModel transform(Tweet tweet) {
    TweetModel model = new TweetModel();
    model.id = tweet.id;
    model.content = tweet.text;
    model.username = tweet.user.screenName;
    model.profile = tweet.user.profileImageUrl;
    model.createdAt = tweet.createdAt;

    return model;
  }

  public TweetModel transform(Cursor cursor) {
    TweetModel model = new TweetModel();
    model.id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
    model.username = cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME));
    model.profile = cursor.getString(cursor.getColumnIndex(COLUMN_PROFILE));
    model.content = cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT));
    model.createdAt = cursor.getString(cursor.getColumnIndex(COLUMN_CREATED_AT));

    return model;
  }
}

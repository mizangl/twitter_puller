package com.mz.twitterpuller.data.model.mapper;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import com.google.gson.Gson;
import com.mz.twitterpuller.data.model.TweetModel;
import com.twitter.sdk.android.core.models.MediaEntity;
import com.twitter.sdk.android.core.models.Tweet;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

import static com.mz.twitterpuller.data.source.local.TweetEntry.COLUMN_CONTENT;
import static com.mz.twitterpuller.data.source.local.TweetEntry.COLUMN_CREATED_AT;
import static com.mz.twitterpuller.data.source.local.TweetEntry.COLUMN_ID;
import static com.mz.twitterpuller.data.source.local.TweetEntry.COLUMN_MEDIA;
import static com.mz.twitterpuller.data.source.local.TweetEntry.COLUMN_PROFILE;
import static com.mz.twitterpuller.data.source.local.TweetEntry.COLUMN_USERNAME;
import static com.mz.twitterpuller.data.source.local.TweetEntry.TABLE_NAME;

@Singleton public class TweetModelMapper {

  private final Gson gson;

  @Inject public TweetModelMapper() {

    gson = new Gson();
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

    if (tweet.extendedEtities != null && tweet.extendedEtities.media != null && !tweet.extendedEtities.media.isEmpty()) {
      List<String> urlsList = new ArrayList<>();
      List<MediaEntity> mediaEntityList = tweet.extendedEtities.media;

      for (MediaEntity media : mediaEntityList) {
        if (!media.type.equals("photo")) continue;
        urlsList.add(media.mediaUrl);
      }
      if (!urlsList.isEmpty()) model.media = urlsList.toArray(new String[urlsList.size()]);
    }

    return model;
  }

  public TweetModel transform(Cursor cursor) {
    TweetModel model = new TweetModel();
    model.id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
    model.username = cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME));
    model.profile = cursor.getString(cursor.getColumnIndex(COLUMN_PROFILE));
    model.content = cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT));
    model.createdAt = cursor.getString(cursor.getColumnIndex(COLUMN_CREATED_AT));

    final String media = cursor.getString(cursor.getColumnIndex(COLUMN_MEDIA));
    model.media = gson.fromJson((TextUtils.isEmpty(media) ? "" : media), String[].class);

    return model;
  }

  public void transformAndInsert(List<TweetModel> models, SQLiteDatabase database) {
    ContentValues values = new ContentValues();
    for (TweetModel model : models) {
      values.put(COLUMN_ID, model.id);
      values.put(COLUMN_USERNAME, model.username);
      values.put(COLUMN_CONTENT, model.content);
      values.put(COLUMN_PROFILE, model.profile);
      values.put(COLUMN_CREATED_AT, model.createdAt);

      final String media = gson.toJson(model.media);
      values.put(COLUMN_MEDIA, media);

      database.insert(TABLE_NAME, null, values);
    }
  }
}

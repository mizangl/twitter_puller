package com.mz.twitterpuller.data.source.local;

import android.provider.BaseColumns;

public abstract class TweetEntry implements BaseColumns{

  public static final String COLUMN_ID = "tweet_id";
  public static final String COLUMN_USERNAME = "username";
  public static final String COLUMN_CONTENT = "content";
  public static final String COLUMN_PROFILE = "profile";
  public static final String COLUMN_CREATED_AT = "created_at";
  public static final String COLUMN_MEDIA = "media";
}

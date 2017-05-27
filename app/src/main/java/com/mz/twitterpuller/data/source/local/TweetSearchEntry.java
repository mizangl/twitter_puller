package com.mz.twitterpuller.data.source.local;

import android.provider.BaseColumns;

public abstract class TweetSearchEntry implements BaseColumns{

  public static final String COLUMN_ID = "docid";
  public static final String COLUMN_CONTENT = "content";
  public static final String COLUMN_USER = "user";
}

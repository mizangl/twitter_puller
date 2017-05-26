package com.mz.twitterpuller.data.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;
import static com.mz.twitterpuller.data.source.local.TweetEntry.COLUMN_CONTENT;
import static com.mz.twitterpuller.data.source.local.TweetEntry.COLUMN_CREATED_AT;
import static com.mz.twitterpuller.data.source.local.TweetEntry.COLUMN_ID;
import static com.mz.twitterpuller.data.source.local.TweetEntry.COLUMN_PROFILE;
import static com.mz.twitterpuller.data.source.local.TweetEntry.COLUMN_USERNAME;
import static com.mz.twitterpuller.data.source.local.TweetEntry.TABLE_NAME;

public class TweetsDbHelper extends SQLiteOpenHelper {

  private final static int VERSION = 1;
  private final static String DB_NAME = "tweets.db";

  private final static String CREATE_TABLE_TWEETS = "CREATE TABLE "
      + TABLE_NAME
      + " ("
      + _ID
      + " INTEGER PRIMARY KEY AUTOINCREMENT, "
      + COLUMN_ID
      + " BIGINT, "
      + COLUMN_PROFILE
      + " TEXT, "
      + COLUMN_USERNAME
      + " TEXT, "
      + COLUMN_CONTENT
      + " TEXT, "
      + COLUMN_CREATED_AT
      + " TEXT, UNIQUE("
      + COLUMN_ID
      + ") ON CONFLICT REPLACE)";

  public TweetsDbHelper(Context context) {
    super(context, DB_NAME, null, VERSION);
  }

  @Override public void onCreate(SQLiteDatabase db) {
    db.execSQL(CREATE_TABLE_TWEETS);
  }

  @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
  }
}

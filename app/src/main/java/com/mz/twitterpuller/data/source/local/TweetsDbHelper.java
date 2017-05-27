package com.mz.twitterpuller.data.source.local;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import com.mz.twitterpuller.data.model.TweetModel;
import com.mz.twitterpuller.data.model.mapper.TweetModelMapper;
import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static com.mz.twitterpuller.data.source.local.TweetEntry.COLUMN_CONTENT;
import static com.mz.twitterpuller.data.source.local.TweetEntry.COLUMN_CREATED_AT;
import static com.mz.twitterpuller.data.source.local.TweetEntry.COLUMN_ID;
import static com.mz.twitterpuller.data.source.local.TweetEntry.COLUMN_MEDIA;
import static com.mz.twitterpuller.data.source.local.TweetEntry.COLUMN_PROFILE;
import static com.mz.twitterpuller.data.source.local.TweetEntry.COLUMN_USERNAME;
import static com.mz.twitterpuller.data.source.local.TweetSearchEntry.COLUMN_USER;
import static com.mz.twitterpuller.data.source.local.TweetsDbHelper.Tables.TWEET_TABLE;
import static com.mz.twitterpuller.data.source.local.TweetsDbHelper.Triggers.FTS_INSERT_TRIGGER;

public class TweetsDbHelper extends SQLiteOpenHelper {
  private final static int VERSION = 1;
  private final static String DB_NAME = "tweets.db";

  private final static String CREATE_TABLE_TWEETS = "CREATE TABLE "
      + TWEET_TABLE
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
      + COLUMN_MEDIA
      + " TEXT, "
      + COLUMN_CREATED_AT
      + " TEXT, UNIQUE("
      + COLUMN_ID
      + ") ON CONFLICT REPLACE)";

  private final static String CREATE_FTS_TABLE_TWEETS = "CREATE VIRTUAL TABLE "
      + Tables.FTS_TWEET_TABLE
      + " USING fts4 (content="
      + TWEET_TABLE
      + ", "
      + TweetSearchEntry.COLUMN_CONTENT
      + ", "
      + COLUMN_USER
      + ")";

  private final static String CREATE_TRIGGER = "CREATE TRIGGER "
      + FTS_INSERT_TRIGGER
      + " AFTER INSERT ON "
      + TWEET_TABLE
      + " BEGIN "
      + "INSERT INTO "
      + Tables.FTS_TWEET_TABLE
      + "("
      + TweetSearchEntry.COLUMN_ID
      + ", "
      + TweetSearchEntry.COLUMN_CONTENT
      + ", "
      + COLUMN_USER
      + ") SELECT "
      + _ID
      + ", "
      + COLUMN_CONTENT
      + ", "
      + COLUMN_USERNAME
      + " FROM "
      + TWEET_TABLE
      + "; END";

  private final static String QUERY_MATCH = "SELECT * FROM "
      + Tables.TWEET_TABLE
      + " WHERE "
      + TweetEntry._ID
      + " IN (SELECT "
      + TweetSearchEntry.COLUMN_ID
      + " FROM "
      + Tables.FTS_TWEET_TABLE
      + " WHERE "
      + Tables.FTS_TWEET_TABLE
      + " MATCH ?)";

  private final String PROJECTION[] = new String[] {
      COLUMN_ID, COLUMN_PROFILE, COLUMN_USERNAME, COLUMN_CONTENT, COLUMN_CREATED_AT,
      TweetEntry.COLUMN_MEDIA
  };

  public TweetsDbHelper(Context context) {
    super(context, DB_NAME, null, VERSION);
  }

  @Override public void onCreate(SQLiteDatabase db) {
    db.execSQL(CREATE_TABLE_TWEETS);
    db.execSQL(CREATE_FTS_TABLE_TWEETS);
    db.execSQL(CREATE_TRIGGER);
  }

  @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
  }

  List<TweetModel> getTweets(TweetModelMapper mapper) {
    List<TweetModel> models = new ArrayList<>();

    SQLiteDatabase database = getReadableDatabase();

    final Cursor cursor =
        database.query(Tables.TWEET_TABLE, PROJECTION, null, null, null, null, COLUMN_ID + " DESC");

    if (cursor == null || cursor.getCount() == 0) {
      if (cursor != null) cursor.close();
      return models;
    } else {
      while (cursor.moveToNext()) {
        TweetModel model = mapper.transform(cursor);
        models.add(model);
      }
      cursor.close();
      database.close();
    }

    return models;
  }

  void save(List<TweetModel> tweets, TweetModelMapper mapper) {
    SQLiteDatabase database = getWritableDatabase();

    database.beginTransaction();
    try {
      mapper.transformAndInsert(tweets, database);
      database.setTransactionSuccessful();
    } finally {
      database.endTransaction();
    }

    database.close();
  }

  List<TweetModel> search(CharSequence match, TweetModelMapper mapper) {
    List<TweetModel> tweets = new ArrayList<>();

    String args = prepareArgs(match);

    String[] selectionArgs = { args };

    SQLiteDatabase database = getReadableDatabase();

    Cursor cursor = database.rawQuery(QUERY_MATCH, selectionArgs);

    if (cursor == null) return tweets;
    if (cursor.getCount() == 0) return tweets;

    while (cursor.moveToNext()) {
      TweetModel model = mapper.transform(cursor);
      tweets.add(model);
    }

    return tweets;
  }

  @NonNull private String prepareArgs(CharSequence match) {
    String[] split = match.toString().split(" ");
    String args = "";
    for (String s : split) {
      args = args.concat(s).concat("*").concat(" ");
    }
    return args;
  }

  public interface Tables {
    String TWEET_TABLE = "tweet";
    String FTS_TWEET_TABLE = "fts_tweet";
  }

  public interface Triggers {
    String FTS_INSERT_TRIGGER = "fts_insert";
  }
}

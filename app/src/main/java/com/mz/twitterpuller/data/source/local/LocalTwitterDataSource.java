package com.mz.twitterpuller.data.source.local;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.VisibleForTesting;
import com.mz.twitterpuller.data.model.TweetModel;
import com.mz.twitterpuller.data.model.mapper.TweetModelMapper;
import com.mz.twitterpuller.data.source.DataSource;
import com.twitter.sdk.android.core.TwitterCore;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static com.mz.twitterpuller.data.source.local.TweetEntry.COLUMN_CONTENT;
import static com.mz.twitterpuller.data.source.local.TweetEntry.COLUMN_CREATED_AT;
import static com.mz.twitterpuller.data.source.local.TweetEntry.COLUMN_ID;
import static com.mz.twitterpuller.data.source.local.TweetEntry.COLUMN_PROFILE;
import static com.mz.twitterpuller.data.source.local.TweetEntry.COLUMN_USERNAME;
import static com.mz.twitterpuller.data.source.local.TweetEntry.TABLE_NAME;

public class LocalTwitterDataSource implements DataSource {

  private final TweetsDbHelper helper;
  private final TweetModelMapper mapper;
  private final String PROJECTION[] =
      { COLUMN_ID, COLUMN_PROFILE, COLUMN_USERNAME, COLUMN_CONTENT, COLUMN_CREATED_AT };

  public LocalTwitterDataSource(Context context, TweetModelMapper mapper) {
    helper = new TweetsDbHelper(context);
    this.mapper = mapper;
  }

  @Override public Observable<Boolean> doLogin(Activity activity) {
    final boolean isLogged =
        TwitterCore.getInstance().getSessionManager().getActiveSession() != null;
    return Observable.just(isLogged);
  }

  @Override
  public Observable<List<TweetModel>> pullTweets(final Integer count, Long since, final Long max) {
    return Observable.create(new ObservableOnSubscribe<List<TweetModel>>() {
      @Override public void subscribe(ObservableEmitter<List<TweetModel>> emitter)
          throws Exception {

        List<TweetModel> models = new ArrayList<>();

        SQLiteDatabase database = helper.getReadableDatabase();

        final Cursor cursor = database.query(TABLE_NAME, PROJECTION, null, null, null, null, null);

        if (cursor == null || cursor.getCount() == 0) {
          if (cursor != null) cursor.close();
          emitter.onNext(models);
          emitter.onComplete();
        } else {
          while (cursor.moveToNext()) {
            TweetModel model = mapper.transform(cursor);
            models.add(model);
          }
          cursor.close();
          database.close();

          emitter.onNext(models);
          emitter.onComplete();
        }
      }
    });
  }

  @Override public void savedLocally(final List<TweetModel> models) {

    SQLiteDatabase database = helper.getWritableDatabase();

    database.beginTransaction();

    try {
      ContentValues values = new ContentValues();
      for (TweetModel model : models) {
        values.put(COLUMN_ID, model.id);
        values.put(COLUMN_USERNAME, model.username);
        values.put(COLUMN_CONTENT, model.content);
        values.put(COLUMN_PROFILE, model.profile);
        values.put(COLUMN_CREATED_AT, model.createdAt);
        database.insert(TABLE_NAME, null, values);
      }
      database.setTransactionSuccessful();
    } finally {
      database.endTransaction();
    }

    database.close();
  }

  public void cleanLocal() {

    SQLiteDatabase database = helper.getWritableDatabase();

    database.delete(TABLE_NAME, null, null);

    database.close();
  }

  @VisibleForTesting public List<TweetModel> getTweetsFromDB() {
    List<TweetModel> models = new ArrayList<>();
    SQLiteDatabase database = helper.getReadableDatabase();

    final Cursor cursor = database.query(TABLE_NAME, PROJECTION, null, null, null, null, null);

    if (cursor == null || cursor.getCount() == 0) {
      if (cursor != null) cursor.close();
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
}

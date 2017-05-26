package com.mz.twitterpuller.internal;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import com.crashlytics.android.Crashlytics;
import com.mz.twitterpuller.BuildConfig;
import com.mz.twitterpuller.data.RepositoryModule;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

public class TwitterPullerApplication extends Application {

  private static final String TWITTER_KEY = "uW32L5DGLrrulyosZF8g1O7cC";
  private static final String TWITTER_SECRET = "4sigDm4Osrl4pOPrRcaXXVCKQyxrUl9KweyD4Tfnt3CSsa8RlP";

  private ApplicationComponent component;

  public static TwitterPullerApplication get(@NonNull Context context) {
    return (TwitterPullerApplication) context.getApplicationContext();
  }

  @Override public void onCreate() {
    super.onCreate();

    createComponent();

    if (BuildConfig.DEBUG) {
      Timber.plant(new Timber.DebugTree());
    }

    TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
    Fabric.with(this, new Crashlytics(), new TwitterCore(authConfig));
  }

  public ApplicationComponent getComponent() {
    return component;
  }

  private void createComponent() {

    component = DaggerApplicationComponent.builder()
        .applicationModule(new ApplicationModule(getApplicationContext()))
        .repositoryModule(new RepositoryModule())
        .build();
  }
}

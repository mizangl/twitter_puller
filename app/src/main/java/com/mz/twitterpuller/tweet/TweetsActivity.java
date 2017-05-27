package com.mz.twitterpuller.tweet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.mz.twitterpuller.R;
import com.mz.twitterpuller.util.BaseActivity;
import javax.inject.Inject;

public class TweetsActivity extends BaseActivity {

  @Inject TweetsPresenter presenter;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_tweets);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    TweetsFragment fragment =
        (TweetsFragment) getSupportFragmentManager().findFragmentById(R.id.container);

    if (fragment == null) {
      fragment = TweetsFragment.newInstance();
      addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.container);
    }

    DaggerTweetsComponent.builder()
        .applicationComponent(getApplicationComponent())
        .tweetsModule(new TweetsModule(fragment))
        .build()
        .inject(this);
  }

}

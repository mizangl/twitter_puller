package com.mz.twitterpuller.tweet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.View;
import com.mz.twitterpuller.R;
import com.mz.twitterpuller.util.BaseActivity;
import javax.inject.Inject;

public class TweetsActivity extends BaseActivity {

  @Inject TweetsPresenter presenter;

  private View appBarView;

  private int toolbarSize;
  private Toolbar toolbar;
  private boolean pendingIntroAnim = true;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_tweets);

    appBarView = findViewById(R.id.app_bar);
    calculateToolbarSize();
    appBarView.setTranslationY(-toolbarSize);


    toolbar = (Toolbar) findViewById(R.id.toolbar);
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

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    showAppBar();
    return super.onCreateOptionsMenu(menu);
  }

  private void showAppBar() {

    int duration = getResources().getInteger(android.R.integer.config_shortAnimTime);

    appBarView.animate().setStartDelay(duration).setDuration(duration).translationY(0).start();
  }

  private void calculateToolbarSize() {
    TypedValue type = new TypedValue();

    if (getTheme().resolveAttribute(android.R.attr.actionBarSize, type, true)) {
      toolbarSize =
          TypedValue.complexToDimensionPixelSize(type.data, getResources().getDisplayMetrics());
    }
  }

}

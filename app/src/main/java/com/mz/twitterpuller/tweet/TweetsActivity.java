package com.mz.twitterpuller.tweet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import com.mz.twitterpuller.R;
import com.mz.twitterpuller.util.BaseActivity;

public class TweetsActivity extends BaseActivity{

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_tweets);
  }
}

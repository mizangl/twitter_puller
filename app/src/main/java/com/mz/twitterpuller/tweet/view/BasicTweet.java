package com.mz.twitterpuller.tweet.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import com.mz.twitterpuller.R;
import com.mz.twitterpuller.data.model.TweetModel;

public class BasicTweet extends BaseTweetView {
  public BasicTweet(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public BasicTweet(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override protected void inflate(@NonNull Context context) {
    LayoutInflater.from(context).inflate(R.layout.view_tweet_basic, this, true);
  }

  @Override
  protected int measureDecorate(int widthMeasureSpec, int widthUsed, int heightMeasureSpec,
      int heightUsed) {

    return 0;
  }

  @Override protected void layoutDecorated(int paddingStart, int currentTop, int contentWidth,
      int contentStart) {

  }

  @Override protected void bindDecorated(@NonNull TweetModel model) {

  }
}

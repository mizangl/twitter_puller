package com.mz.twitterpuller.tweet.view;

import com.mz.twitterpuller.tweet.TweetAdapterItem;

public class ProgressItem implements TweetAdapterItem<Void> {
  @Override public int getType() {
    return PROGRESS;
  }

  @Override public Void getModel() {
    return null;
  }
}

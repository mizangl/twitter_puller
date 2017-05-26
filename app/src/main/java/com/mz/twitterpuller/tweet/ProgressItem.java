package com.mz.twitterpuller.tweet;

public class ProgressItem implements TweetAdapterItem<Void> {
  @Override public int getType() {
    return PROGRESS;
  }

  @Override public Void getModel() {
    return null;
  }
}

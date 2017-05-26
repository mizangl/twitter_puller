package com.mz.twitterpuller.tweet;

import com.mz.twitterpuller.data.model.TweetModel;

class TweetItem implements TweetAdapterItem<TweetModel> {

  private final TweetModel model;

  public TweetItem(TweetModel model) {
    this.model = model;
  }

  @Override public int getType() {
    return TWEET;
  }

  @Override public TweetModel getModel() {
    return model;
  }

}

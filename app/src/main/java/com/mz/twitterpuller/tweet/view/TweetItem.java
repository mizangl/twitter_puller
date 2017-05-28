package com.mz.twitterpuller.tweet.view;

import android.support.annotation.NonNull;
import com.mz.twitterpuller.data.model.TweetModel;
import com.mz.twitterpuller.tweet.TweetAdapterItem;

public class TweetItem implements TweetAdapterItem<TweetModel> {

  private final TweetModel model;
  private final int type;

  public TweetItem(@NonNull TweetModel model, int type) {
    this.model = model;
    this.type = type;
  }

  @Override public int getType() {
    return type;
  }

  @Override public TweetModel getModel() {
    return model;
  }
}

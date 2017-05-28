package com.mz.twitterpuller.tweet.view;

import android.view.View;
import com.mz.twitterpuller.data.model.TweetModel;

public class TweetViewHolder extends ViewHolder<TweetModel> {
  public TweetViewHolder(View itemView) {
    super(itemView);
  }

  public void bind(TweetModel model) {
    ((BaseTweetView) itemView).bind(model);
  }
}

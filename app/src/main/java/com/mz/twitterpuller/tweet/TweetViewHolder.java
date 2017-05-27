package com.mz.twitterpuller.tweet;

import android.view.View;
import com.mz.twitterpuller.data.model.TweetModel;

class TweetViewHolder extends ViewHolder<TweetModel> {

  private BaseTweetView view;

  TweetViewHolder(View itemView) {
    super(itemView);
    view = (BaseTweetView) itemView;
  }

  void bind(TweetModel model) {
    view.bind(model);
  }
}

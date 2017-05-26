package com.mz.twitterpuller.tweet;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.mz.twitterpuller.data.model.TweetModel;

class TweetViewHolder extends ViewHolder<TweetModel> {

  private TweetView view;

  TweetViewHolder(View itemView) {
    super(itemView);
    view = (TweetView) itemView;
  }

  void bind(TweetModel model) {
    view.bind(model);
  }
}

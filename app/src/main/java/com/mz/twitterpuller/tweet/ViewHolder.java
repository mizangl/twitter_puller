package com.mz.twitterpuller.tweet;

import android.support.v7.widget.RecyclerView;
import android.view.View;

abstract class ViewHolder<T> extends RecyclerView.ViewHolder {

  public ViewHolder(View itemView) {
    super(itemView);
  }

  abstract void bind(T model);
}

package com.mz.twitterpuller.tweet;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.mz.twitterpuller.R;
import com.mz.twitterpuller.data.model.TweetModel;
import java.util.ArrayList;
import java.util.List;

import static com.mz.twitterpuller.tweet.TweetAdapterItem.PROGRESS;

class TweetAdapter extends RecyclerView.Adapter<ViewHolder> {

  private final Context context;
  private List<TweetAdapterItem> data = new ArrayList<>();

  TweetAdapter(Context context) {
    this.context = context;
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    if (viewType == PROGRESS) {
      return new ProgressViewHolder(
          LayoutInflater.from(context).inflate(R.layout.layout_item_progress, parent, false));
    } else {
      return new TweetViewHolder(
          LayoutInflater.from(context).inflate(R.layout.layout_tweet, parent, false));
    }
  }

  @Override public void onBindViewHolder(ViewHolder holder, int position) {
    if (getItemViewType(position) == PROGRESS) {
      return;
    }
    //noinspection unchecked
    holder.bind(((TweetItem) data.get(position)).getModel());
  }

  @Override public int getItemViewType(int position) {
    return data.get(position).getType();
  }

  @Override public int getItemCount() {
    return data.size();
  }

  void addTweet(@NonNull TweetModel value) {
    data.add(new TweetItem(value));
  }

  void addTweets(@NonNull List<TweetModel> values) {
    if (values != null && !values.isEmpty()) {
      for (TweetModel model : values) {
        data.add(new TweetItem(model));
        notifyItemInserted(data.size() - 1);
      }
    }
  }

  void showProgress() {
    data.add(new ProgressItem());
    notifyItemInserted(data.size() - 1);
  }

  void removeProgress() {
    int index = data.size() - 1;
    data.remove(index);
    notifyItemRemoved(index);
  }
}

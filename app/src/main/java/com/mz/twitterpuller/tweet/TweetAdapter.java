package com.mz.twitterpuller.tweet;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.mz.twitterpuller.R;
import com.mz.twitterpuller.data.model.TweetModel;
import com.mz.twitterpuller.tweet.view.ProgressItem;
import com.mz.twitterpuller.tweet.view.TweetItem;
import com.mz.twitterpuller.tweet.view.TweetViewHolder;
import com.mz.twitterpuller.tweet.view.ViewHolder;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import static com.mz.twitterpuller.tweet.TweetAdapterItem.PROGRESS;
import static com.mz.twitterpuller.tweet.TweetAdapterItem.TWEET_FOUR_IMAGE;
import static com.mz.twitterpuller.tweet.TweetAdapterItem.TWEET_ONE_IMAGE;
import static com.mz.twitterpuller.tweet.TweetAdapterItem.TWEET_THREE_IMAGE;
import static com.mz.twitterpuller.tweet.TweetAdapterItem.TWEET_TWO_IMAGE;

class TweetAdapter extends RecyclerView.Adapter<ViewHolder> {

  private final Context context;
  private List<TweetAdapterItem> data = new ArrayList<>();
  private List<TweetAdapterItem> cache = new ArrayList<>();

  TweetAdapter(Context context) {
    this.context = context;
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    if (viewType == PROGRESS) {
      return new ProgressViewHolder(
          LayoutInflater.from(context).inflate(R.layout.layout_item_progress, parent, false));
    } else {
      switch (viewType) {
        case TweetAdapterItem.TWEET_BASIC:
          return new TweetViewHolder(
              LayoutInflater.from(context).inflate(R.layout.layout_tweet_basic, parent, false));
        case TWEET_ONE_IMAGE:
          return new TweetViewHolder(
              LayoutInflater.from(context).inflate(R.layout.layout_tweet_one_image, parent, false));
        case TWEET_TWO_IMAGE:
          return new TweetViewHolder(
              LayoutInflater.from(context).inflate(R.layout.layout_tweet_two_image, parent, false));

        case TWEET_THREE_IMAGE:
          return new TweetViewHolder(
              LayoutInflater.from(context).inflate(R.layout.layout_tweet_three_image, parent, false));

        case TWEET_FOUR_IMAGE:
          return new TweetViewHolder(
              LayoutInflater.from(context).inflate(R.layout.layout_tweet_four_image, parent, false));

        default:
          throw new UnsupportedOperationException();
      }
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
    data.add(new TweetItem(value, value.getMedia().length));
  }

  void showFiltered(@NonNull List<TweetModel> filtered) {
    if (cache.isEmpty()) cache.addAll(data);

    removeItems();

    addTweets(filtered);
  }

  void removeFiltered() {
    removeItems();

    data.addAll(cache);

    cache.clear();
  }

  void addTweets(@NonNull List<TweetModel> values) {
    if (!values.isEmpty()) {
      for (TweetModel model : values) {
        data.add(new TweetItem(model, model.getMedia().length));
        notifyItemInserted(data.size() - 1);
      }
    }
  }

  void addTweetsTop(@NonNull List<TweetModel> values) {
    if (!values.isEmpty()) {
      ListIterator<TweetModel> iterator = values.listIterator(values.size());
      TweetModel model;
      while (iterator.hasPrevious()) {
        model = iterator.previous();
        data.add(0, new TweetItem(model, model.getMedia().length));
        notifyItemInserted(0);
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

  private void removeItems() {
    int latest = data.size() - 1;
    data.clear();
    notifyDataSetChanged();
  }
}

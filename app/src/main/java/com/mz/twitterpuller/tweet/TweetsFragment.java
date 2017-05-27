package com.mz.twitterpuller.tweet;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import com.mz.twitterpuller.R;
import com.mz.twitterpuller.data.model.TweetModel;
import com.mz.twitterpuller.tweet.TweetsContract.View;
import com.mz.twitterpuller.util.EndlessScroll;
import java.util.List;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class TweetsFragment extends Fragment implements View {

  private TweetsContract.Presenter presenter;

  private ProgressBar progressBar;
  private RecyclerView recyclerView;

  private TweetAdapter adapter;

  public static TweetsFragment newInstance() {

    return new TweetsFragment();
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
  }

  @Nullable @Override
  public android.view.View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_tweets, container, false);
  }

  @Override public void onViewCreated(android.view.View view, @Nullable Bundle savedInstanceState) {

    recyclerView = (RecyclerView) view.findViewById(R.id.recycle_view);
    progressBar = (ProgressBar) view.findViewById(R.id.progress);
  }

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    presenter.restoreInstance(savedInstanceState);

    setupRecycleView();
  }

  @Override public void onResume() {
    super.onResume();
    presenter.start();
  }

  @Override public void onSaveInstanceState(Bundle outState) {
    presenter.saveInstance(outState);
    super.onSaveInstanceState(outState);
  }

  @Override public void setPresenter(@NonNull TweetsContract.Presenter presenter) {
    this.presenter = presenter;
  }

  @Override public void setProgressIndicator(boolean status) {
    progressBar.setVisibility(status ? VISIBLE : INVISIBLE);
  }

  @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.tweets_menu, menu);
  }

  @Override public void bind(TweetModel value) {
    adapter.addTweet(value);
  }

  @Override public void bind(List<TweetModel> values) {
    adapter.addTweets(values);
  }

  @Override public void addProgress() {
    adapter.showProgress();
  }

  @Override public void removeProgress() {
    adapter.removeProgress();
  }

  @NonNull private EndlessScroll createEndlessScroll(LinearLayoutManager manager) {
    return new EndlessScroll(manager) {
      @Override public void onScrolledToEnd(int firstItemPosition) {
        presenter.pullOlder();
      }
    };
  }

  private void setupRecycleView() {
    adapter = new TweetAdapter(getActivity());
    LinearLayoutManager manager = new LinearLayoutManager(getActivity());
    recyclerView.setLayoutManager(manager);
    recyclerView.setAdapter(adapter);
    recyclerView.addOnScrollListener(createEndlessScroll(manager));
  }
}

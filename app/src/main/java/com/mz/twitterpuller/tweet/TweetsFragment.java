package com.mz.twitterpuller.tweet;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.ViewGroup;
import com.mz.twitterpuller.R;
import com.mz.twitterpuller.data.model.TweetModel;
import com.mz.twitterpuller.tweet.TweetsContract.View;
import com.mz.twitterpuller.util.EndlessScroll;
import java.util.List;
import timber.log.Timber;

public class TweetsFragment extends Fragment
    implements View, SearchView.OnQueryTextListener, SwipeRefreshLayout.OnRefreshListener {

  private TweetsContract.Presenter presenter;

  private RecyclerView recyclerView;
  private SwipeRefreshLayout swipeRefreshLayout;

  private TweetAdapter adapter;

  public static TweetsFragment newInstance() {

    return new TweetsFragment();
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
    Timber.tag(TweetsFragment.class.getSimpleName());
  }

  @Nullable @Override
  public android.view.View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_tweets, container, false);
  }

  @Override public void onViewCreated(android.view.View view, @Nullable Bundle savedInstanceState) {

    recyclerView = (RecyclerView) view.findViewById(R.id.recycle_view);
    swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
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
    swipeRefreshLayout.setRefreshing(status);
  }

  @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.tweets_menu, menu);
  }

  @Override public void onPrepareOptionsMenu(Menu menu) {
    SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

    searchView.setOnQueryTextListener(this);
  }

  @Override public void bind(TweetModel value) {
    adapter.addTweet(value);
  }

  @Override public void bind(List<TweetModel> values) {
    adapter.addTweets(values);
  }

  @Override public void bindFiltered(List<TweetModel> values) {
    Timber.d("Filtered %s", values);
    adapter.showFiltered(values);
  }

  @Override public void removeFiltered() {
    adapter.removeFiltered();
  }

  @Override public void addProgress() {
    adapter.showProgress();
  }

  @Override public void removeProgress() {
    adapter.removeProgress();
  }

  @Override public boolean onQueryTextSubmit(String query) {
    Timber.d("onQueryTextSubmit %s", query);
    return false;
  }

  @Override public boolean onQueryTextChange(String newText) {
    Timber.d("onQueryTextChange %s", newText);
    presenter.search(newText);
    return false;
  }

  @Override public void onRefresh() {
    presenter.pullNews();
  }

  @Override public void bindTop(List<TweetModel> values) {
    adapter.addTweetsTop(values);
  }

  @NonNull private EndlessScroll createEndlessScroll(LinearLayoutManager manager) {
    return new EndlessScroll(manager) {
      @Override public void onScrolledToEnd(int firstItemPosition) {
        presenter.pullOlder();
      }
    };
  }

  private void setupRecycleView() {
    swipeRefreshLayout.setColorSchemeColors(
        ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null),
        ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark, null),
        ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));

    adapter = new TweetAdapter(getActivity());
    LinearLayoutManager manager = new LinearLayoutManager(getActivity());
    recyclerView.setLayoutManager(manager);
    recyclerView.setAdapter(adapter);
    recyclerView.addOnScrollListener(createEndlessScroll(manager));
    swipeRefreshLayout.setOnRefreshListener(this);
  }
}

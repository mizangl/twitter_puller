package com.mz.twitterpuller.util;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class EndlessScroll extends RecyclerView.OnScrollListener {

  private final LinearLayoutManager manager;

  protected EndlessScroll(LinearLayoutManager manager) {
    this.manager = manager;
  }

  @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
    super.onScrolled(recyclerView, dx, dy);
    if (canLoadMore()) {
      onScrolledToEnd(manager.findFirstVisibleItemPosition());
    }
  }

  protected void refreshRecycle(RecyclerView recyclerView, RecyclerView.Adapter adapter,
      int position) {
    recyclerView.setAdapter(adapter);
    recyclerView.invalidate();
    recyclerView.scrollToPosition(position);
  }

  private boolean canLoadMore() {
    final int childCount = manager.getChildCount();
    final int itemCount = manager.getItemCount();
    final int firstVisibleItemPosition = manager.findFirstVisibleItemPosition();

    final boolean latest = childCount + firstVisibleItemPosition >= itemCount;

    return latest;
  }

  public abstract void onScrolledToEnd(int firstItemPosition);
}

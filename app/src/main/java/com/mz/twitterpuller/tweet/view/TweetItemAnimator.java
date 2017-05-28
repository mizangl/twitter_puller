package com.mz.twitterpuller.tweet.view;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import com.mz.twitterpuller.util.DefaultAnimatorListener;

public class TweetItemAnimator extends DefaultItemAnimator {

  public static final int DURATION = 300;
  private int height;
  private int animateItemLeft = -4;

  private final Interpolator interpolator = new DecelerateInterpolator();

  @Override public boolean canReuseUpdatedViewHolder(@NonNull RecyclerView.ViewHolder viewHolder) {
    return true;
  }

  @Override public boolean animateAdd(RecyclerView.ViewHolder holder) {
    if (holder.getLayoutPosition() > animateItemLeft) {
      animateItemLeft++;
      addAnimation(holder);
      return false;
    }
    dispatchAddFinished(holder);
    return false;
  }

  private void addAnimation(final RecyclerView.ViewHolder holder) {
    holder.itemView.setTranslationY(getScreenHeight(holder.itemView.getContext()));
    holder.itemView.animate()
        .translationY(0)
        .setInterpolator(interpolator)
        .setDuration(DURATION)
        .setListener(new DefaultAnimatorListener() {
          @Override public void onAnimationEnd(Animator animation) {
            dispatchAddFinished(holder);
          }
        })
        .start();
  }

  private int getScreenHeight(Context c) {
    if (height == 0) {
      WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
      Display display = wm.getDefaultDisplay();
      Point size = new Point();
      display.getSize(size);
      height = size.y;
    }

    return height;
  }
}

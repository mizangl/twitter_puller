package com.mz.twitterpuller.tweet;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.request.target.Target;
import com.mz.twitterpuller.R;
import com.mz.twitterpuller.data.model.TweetModel;
import com.mz.twitterpuller.internal.GlideApp;

public abstract class BaseTweetView extends CardView {

  private final TextView usernameView;
  private final TextView contentView;
  private final ImageView profileView;
  private Target<Bitmap> bitmapTarget;

  public BaseTweetView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public BaseTweetView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);

    inflate(context);

    usernameView = (TextView) findViewById(R.id.username);
    contentView = (TextView) findViewById(R.id.content);
    profileView = (ImageView) findViewById(R.id.profile);
  }

  protected abstract void inflate(@NonNull Context context);

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    final int widthSize = MeasureSpec.getSize(widthMeasureSpec);

    int widthUsed = 0;
    int heightUsed = 0;

    measureChildWithMargins(profileView, widthMeasureSpec, widthUsed, heightMeasureSpec,
        heightUsed);

    widthUsed += calculateMeasuredWidthWithMargins(profileView);

    measureChildWithMargins(usernameView, widthMeasureSpec, widthUsed, heightMeasureSpec,
        heightUsed);

    heightUsed += calculateMeasuredHeightWithMargins(usernameView);

    measureChildWithMargins(contentView, widthMeasureSpec, widthUsed, heightMeasureSpec,
        heightUsed);
    heightUsed += calculateMeasuredHeightWithMargins(contentView);

    heightUsed += measureDecorate(widthMeasureSpec, widthUsed, heightMeasureSpec, heightUsed);

    int heightSize = heightUsed + getPaddingTop() + getPaddingBottom();
    setMeasuredDimension(widthSize, heightSize);
  }

  protected abstract int measureDecorate(int widthMeasureSpec, int widthUsed,
      int heightMeasureSpec, int heightUsed);

  @Override protected void onLayout(boolean changed, int l, int t, int r, int b) {
    final int paddingStart = getPaddingStart();

    int currentTop = getPaddingTop();

    layoutView(profileView, paddingStart, currentTop, profileView.getMeasuredWidth(),
        profileView.getMeasuredHeight());

    final int contentStart = calculateWidthWithMargins(profileView) + paddingStart;
    final int contentWidth = r - l - contentStart - getPaddingEnd();

    layoutView(usernameView, contentStart, currentTop, contentWidth,
        usernameView.getMeasuredHeight());

    currentTop += calculateHeightWithMargins(usernameView);

    layoutView(contentView, contentStart, currentTop, contentWidth,
        contentView.getMeasuredHeight());

    currentTop += calculateHeightWithMargins(contentView);

    layoutDecorated(paddingStart, currentTop, contentWidth, contentStart);
  }

  protected abstract void layoutDecorated(int paddingStart, int currentTop, int contentWidth,
      int contentStart);

  @Override protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    if (bitmapTarget != null) {
      bitmapTarget.onStop();
    }
  }

  void bind(TweetModel model) {
    usernameView.setText(model.username);
    contentView.setText(model.content);
    bitmapTarget = GlideApp.with(getContext()).asBitmap().load(model.profile).into(profileView);

    bindDecorated(model);
  }

  protected abstract void bindDecorated(@NonNull TweetModel model);

  protected void layoutView(View view, int left, int top, int width, int height) {
    MarginLayoutParams margins = (MarginLayoutParams) view.getLayoutParams();
    final int leftWithMargins = left + margins.leftMargin;
    final int topWithMargins = top + margins.topMargin;

    view.layout(leftWithMargins, topWithMargins, leftWithMargins + width, topWithMargins + height);
  }

  protected int calculateWidthWithMargins(View child) {
    final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
    return child.getWidth() + lp.leftMargin + lp.rightMargin;
  }

  protected int calculateHeightWithMargins(View child) {
    final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
    return child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
  }

  protected int calculateMeasuredWidthWithMargins(View child) {
    final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
    return child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
  }

  protected int calculateMeasuredHeightWithMargins(View child) {
    final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
    return child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
  }
}

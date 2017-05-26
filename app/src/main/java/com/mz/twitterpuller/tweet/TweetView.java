package com.mz.twitterpuller.tweet;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.request.target.Target;
import com.mz.twitterpuller.R;
import com.mz.twitterpuller.data.model.TweetModel;
import com.mz.twitterpuller.internal.GlideApp;

public class TweetView extends CardView {

  private final TextView usernameView;
  private final TextView contentView;
  private final ImageView profileView;

  private Target<Bitmap> bitmapTarget;

  public TweetView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public TweetView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);

    LayoutInflater.from(context).inflate(R.layout.view_tweet, this, true);

    usernameView = (TextView) findViewById(R.id.username);
    contentView = (TextView) findViewById(R.id.content);
    profileView = (ImageView) findViewById(R.id.profile);
  }

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

    int heightSize = heightUsed + getPaddingTop() + getPaddingBottom();
    setMeasuredDimension(widthSize, heightSize);
  }

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
  }

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
  }

  private void layoutView(View view, int left, int top, int width, int height) {
    MarginLayoutParams margins = (MarginLayoutParams) view.getLayoutParams();
    final int leftWithMargins = left + margins.leftMargin;
    final int topWithMargins = top + margins.topMargin;

    view.layout(leftWithMargins, topWithMargins, leftWithMargins + width, topWithMargins + height);
  }

  private int calculateWidthWithMargins(View child) {
    final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
    return child.getWidth() + lp.leftMargin + lp.rightMargin;
  }

  private int calculateHeightWithMargins(View child) {
    final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
    return child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
  }

  private int calculateMeasuredWidthWithMargins(View child) {
    final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
    return child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
  }

  private int calculateMeasuredHeightWithMargins(View child) {
    final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
    return child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
  }
}

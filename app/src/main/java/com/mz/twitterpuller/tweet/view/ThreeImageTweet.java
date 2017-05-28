package com.mz.twitterpuller.tweet.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import com.bumptech.glide.request.target.Target;
import com.mz.twitterpuller.R;
import com.mz.twitterpuller.data.model.TweetModel;
import com.mz.twitterpuller.internal.GlideApp;

public class ThreeImageTweet extends BaseTweetView {

  private final ImageView mediaImage1;
  private final ImageView mediaImage2;
  private final ImageView mediaImage3;
  private Target<Bitmap> mediaTarget1;
  private Target<Bitmap> mediaTarget2;
  private Target<Bitmap> mediaTarget3;

  public ThreeImageTweet(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public ThreeImageTweet(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);

    mediaImage1 = (ImageView) findViewById(R.id.media);
    mediaImage2 = (ImageView) findViewById(R.id.media2);
    mediaImage3 = (ImageView) findViewById(R.id.media3);
  }

  @Override protected void inflate(@NonNull Context context) {
    LayoutInflater.from(context).inflate(R.layout.view_tweet_with_three_image, this, true);
  }

  @Override
  protected int measureDecorate(int widthMeasureSpec, int widthUsed, int heightMeasureSpec,
      int heightUsed) {

    if (mediaImage1.getVisibility() != GONE) {
      measureChildWithMargins(mediaImage1, widthMeasureSpec, widthUsed, heightMeasureSpec,
          heightUsed);
    }

    if (mediaImage2.getVisibility() != GONE) {
      measureChildWithMargins(mediaImage2, widthMeasureSpec, widthUsed, heightMeasureSpec,
          heightUsed);
    }

    if (mediaImage3.getVisibility() != GONE) {
      measureChildWithMargins(mediaImage3, widthMeasureSpec, widthUsed, heightMeasureSpec,
          heightUsed);
    }

    heightUsed += calculateMeasuredHeightWithMargins(mediaImage1);
    return heightUsed;
  }

  @Override protected void layoutDecorated(int paddingStart, int currentTop, int contentWidth,
      int contentStart) {

    contentStart = getPaddingStart() + getPaddingEnd();

    int middleTop = currentTop;

    if (mediaImage1.getVisibility() != GONE) {
      layoutView(mediaImage1, contentStart, currentTop, contentWidth / 2,
          mediaImage1.getMeasuredHeight() / 2);

      middleTop += calculateHeightWithMargins(mediaImage1) / 2;

    }
    if (mediaImage2.getVisibility() != GONE) {
      layoutView(mediaImage2, contentStart, middleTop, contentWidth / 2,
          mediaImage2.getMeasuredHeight() / 2);
    }

    contentStart += calculateWidthWithMargins(mediaImage1);

    if (mediaImage3.getVisibility() != GONE) {
      layoutView(mediaImage3, contentStart, currentTop, contentWidth / 2,
          mediaImage3.getMeasuredHeight());
    }
  }

  @Override protected void bindDecorated(@NonNull TweetModel model) {

    String url = model.media[0];
    String url2 = model.media[1];
    String url3 = model.media[2];
    mediaTarget1 = GlideApp.with(getContext()).asBitmap().load(url).into(mediaImage1);
    mediaTarget2 = GlideApp.with(getContext()).asBitmap().load(url2).into(mediaImage2);
    mediaTarget3 = GlideApp.with(getContext()).asBitmap().load(url3).into(mediaImage3);
  }

  @Override protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    if (mediaTarget1 != null) mediaTarget1.onStop();
    if (mediaTarget2 != null) mediaTarget2.onStop();
  }
}

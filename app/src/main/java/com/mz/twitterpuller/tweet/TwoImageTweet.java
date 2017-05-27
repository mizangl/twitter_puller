package com.mz.twitterpuller.tweet;

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

public class TwoImageTweet extends BaseTweetView {

  private final ImageView mediaImage1;
  private final ImageView mediaImage2;
  private Target<Bitmap> mediaTarget1;
  private Target<Bitmap> mediaTarget2;

  public TwoImageTweet(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public TwoImageTweet(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);

    mediaImage1 = (ImageView) findViewById(R.id.media);
    mediaImage2 = (ImageView) findViewById(R.id.media2);
  }

  @Override protected void inflate(@NonNull Context context) {
    LayoutInflater.from(context).inflate(R.layout.view_tweet_with_two_image, this, true);
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

    heightUsed += calculateMeasuredHeightWithMargins(mediaImage1);
    return heightUsed;
  }

  @Override protected void layoutDecorated(int paddingStart, int currentTop, int contentWidth,
      int contentStart) {

    contentStart = getPaddingStart() + getPaddingEnd();

    if (mediaImage1.getVisibility() != GONE) {
      layoutView(mediaImage1, contentStart, currentTop, contentWidth / 2,
          mediaImage1.getMeasuredHeight());

      contentStart += calculateWidthWithMargins(mediaImage1);
    }
    if (mediaImage2.getVisibility() != GONE) {
      layoutView(mediaImage2, contentStart, currentTop, contentWidth / 2,
          mediaImage2.getMeasuredHeight());
    }
  }

  @Override protected void bindDecorated(@NonNull TweetModel model) {
    String url = model.media[0];
    String url2 = model.media[1];
    mediaTarget1 = GlideApp.with(getContext()).asBitmap().load(url).into(mediaImage1);
    mediaTarget2 = GlideApp.with(getContext()).asBitmap().load(url2).into(mediaImage2);
  }

  @Override protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    if (mediaTarget1 != null) mediaTarget1.onStop();
    if (mediaTarget2 != null) mediaTarget2.onStop();
  }
}

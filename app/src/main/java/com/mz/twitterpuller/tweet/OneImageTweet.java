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

public class OneImageTweet extends BaseTweetView {

  private final ImageView mediaImage;
  private Target<Bitmap> mediaTarget;

  public OneImageTweet(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public OneImageTweet(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);

    mediaImage = (ImageView) findViewById(R.id.media);
  }

  @Override protected void inflate(@NonNull Context context) {
    LayoutInflater.from(context).inflate(R.layout.view_tweet_with_one_image, this, true);
  }

  @Override
  protected int measureDecorate(int widthMeasureSpec, int widthUsed, int heightMeasureSpec,
      int heightUsed) {

    if (mediaImage.getVisibility() != GONE) {
      measureChildWithMargins(mediaImage, widthMeasureSpec, widthUsed, heightMeasureSpec,
          heightUsed);
      return calculateMeasuredHeightWithMargins(mediaImage);
    }
    return heightUsed;
  }

  @Override protected void layoutDecorated(int paddingStart, int currentTop, int contentWidth,
      int contentStart) {

    if (mediaImage.getVisibility() != GONE) {
      layoutView(mediaImage, contentStart, currentTop, contentWidth,
          mediaImage.getMeasuredHeight());
    }
  }

  @Override protected void bindDecorated(@NonNull TweetModel model) {

    String url = model.media[0];
    mediaTarget = GlideApp.with(getContext()).asBitmap().load(url).into(mediaImage);
  }

  @Override protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    if (mediaTarget != null) mediaTarget.onStop();
  }
}

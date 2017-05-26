package com.mz.twitterpuller.tweet;

import android.os.Bundle;
import com.mz.twitterpuller.data.model.TweetModel;
import com.mz.twitterpuller.util.BasePresenter;
import com.mz.twitterpuller.util.BaseView;
import java.util.Map;

public interface TweetsContract {

  interface View extends BaseView<Presenter>{

    String EXTRA_PROGRESS = "extra:progress";
    String EXTRA_SINCE = "extra:since";
    String EXTRA_MAX = "extra:max";

    void setProgressIndicator(boolean status);

    void bind(TweetModel value);

    void updateSinceAndMax();

    void removeProgress();

    void addProgress();

  }

  interface Presenter extends BasePresenter{

    void saveInstance(Bundle bundle);

    void restoreInstance(Bundle savedInstanceState);

    void updateSinceAndMax(long first, long last);

    void pullNews();

    void pullOlder();
  }
}

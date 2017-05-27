package com.mz.twitterpuller.tweet;

import android.os.Bundle;
import com.mz.twitterpuller.data.model.TweetModel;
import com.mz.twitterpuller.util.BasePresenter;
import com.mz.twitterpuller.util.BaseView;
import java.util.List;
import java.util.Map;

public interface TweetsContract {

  interface View extends BaseView<Presenter>{

    String EXTRA_PROGRESS = "extra:progress";
    String EXTRA_FILTERED = "extra:filtered";
    String EXTRA_SINCE = "extra:since";
    String EXTRA_MAX = "extra:max";

    void setProgressIndicator(boolean status);

    void bind(TweetModel value);

    void removeProgress();

    void addProgress();

    void bind(List<TweetModel> values);

    void bindFiltered(List<TweetModel> values);

    void removeFiltered();
  }

  interface Presenter extends BasePresenter{

    void saveInstance(Bundle bundle);

    void restoreInstance(Bundle savedInstanceState);

    void pullNews();

    void pullOlder();

    void search(CharSequence newText);
  }
}

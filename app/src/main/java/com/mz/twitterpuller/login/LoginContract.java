package com.mz.twitterpuller.login;

import android.app.Activity;
import android.content.Intent;
import com.mz.twitterpuller.util.BasePresenter;
import com.mz.twitterpuller.util.BaseView;

public interface LoginContract {

  interface View extends BaseView<Presenter> {

    void setProgressIndicator(boolean visible);

    void navigateToMain();

    void onActivityResult(int requestCode, int resultCode, Intent data);

    Activity getActivity();

  }

  interface Presenter extends BasePresenter {

    void login();

    void onActivityResult(int requestCode, int resultCode, Intent data);
  }
}

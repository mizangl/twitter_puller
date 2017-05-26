package com.mz.twitterpuller.login;

import android.app.Activity;
import android.content.Intent;
import com.mz.twitterpuller.interactor.DefaultObserver;
import com.mz.twitterpuller.interactor.Interactor;
import javax.inject.Inject;
import javax.inject.Named;

final class LoginPresenter implements LoginContract.Presenter {

  private final Interactor<Boolean, Activity> getSessionInteractor;
  private final LoginContract.View loginView;

  @Inject LoginPresenter(@Named("session") Interactor getSessionInteractor,
      LoginContract.View loginView) {
    this.getSessionInteractor = getSessionInteractor;
    this.loginView = loginView;
  }

  @Inject void setUpView() {
    loginView.setPresenter(this);
  }

  @Override public void start() {
    loginView.setProgressIndicator(true);
    getSessionInteractor.execute(new SessionObserver(), null);
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    loginView.onActivityResult(requestCode, resultCode, data);
  }

  @Override public void login() {
    getSessionInteractor.execute(new SessionObserver(), loginView.getActivity());
  }

  private class SessionObserver extends DefaultObserver<Boolean> {

    @Override protected void onStart() {
      loginView.setProgressIndicator(true);
    }

    @Override public void onNext(Boolean value) {
      if (value) {
        loginView.navigateToMain();
      }
    }

    @Override public void onError(Throwable e) {
      loginView.setProgressIndicator(false);
    }

    @Override public void onComplete() {
      loginView.setProgressIndicator(false);
    }
  }
}

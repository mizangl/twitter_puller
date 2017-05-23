package com.mz.twitterpuller.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import com.mz.twitterpuller.R;
import com.mz.twitterpuller.util.BaseActivity;
import javax.inject.Inject;

public class LoginActivity extends BaseActivity {

  @Inject LoginPresenter presenter;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    LoginFragment loginFragment =
        (LoginFragment) getSupportFragmentManager().findFragmentById(R.id.container);

    if (loginFragment == null) {
      loginFragment = LoginFragment.newInstance();
      BaseActivity.addFragmentToActivity(getSupportFragmentManager(), loginFragment,
          R.id.container);
    }

    DaggerLoginComponent.builder()
        .applicationComponent(getApplicationComponent())
        .loginModule(new LoginModule(loginFragment))
        .build()
        .inject(this);
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    presenter.onActivityResult(requestCode, resultCode, data);
  }
}


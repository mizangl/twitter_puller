package com.mz.twitterpuller.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import com.mz.twitterpuller.R;
import com.mz.twitterpuller.tweet.TweetsActivity;
import com.mz.twitterpuller.util.DefaultAnimListener;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import timber.log.Timber;

public class LoginFragment extends Fragment implements LoginContract.View {

  private TwitterLoginButton twitterLoginButton;
  private ImageButton loginButton;
  private LoginContract.Presenter presenter;

  public static LoginFragment newInstance() {
    return new LoginFragment();
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Timber.tag(LoginFragment.class.getSimpleName());
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_login, container, false);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    loginButton = (ImageButton) view.findViewById(R.id.login);
    twitterLoginButton = (TwitterLoginButton) view.findViewById(R.id.login_tw);

    loginButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        presenter.login();
      }
    });
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    twitterLoginButton.onActivityResult(requestCode, resultCode, data);
  }

  @Override public void setPresenter(@NonNull LoginContract.Presenter presenter) {
    this.presenter = presenter;
  }

  @Override public void setProgressIndicator(boolean visible) {
    loginButton.setVisibility(visible ? View.INVISIBLE : View.VISIBLE);
  }

  @Override public void navigateToMain() {
    Timber.i("Navigate to Main");
    final Intent intent = new Intent(getActivity(), TweetsActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(intent);
    getActivity().finish();
  }

  @Override public void onResume() {
    super.onResume();
    showLoginAnim();
  }

  private void showLoginAnim() {
    Animation loginAnim =
        AnimationUtils.loadAnimation(getActivity().getApplication(), R.anim.login_btn_anim);

    loginAnim.setInterpolator(new AccelerateInterpolator());

    loginAnim.setAnimationListener(new DefaultAnimListener() {
      @Override public void onAnimationEnd(Animation animation) {
        if (presenter != null) presenter.start();
      }
    });

    loginButton.startAnimation(loginAnim);
  }
}

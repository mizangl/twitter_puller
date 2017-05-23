package com.mz.twitterpuller.util;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import com.mz.twitterpuller.internal.ApplicationComponent;
import com.mz.twitterpuller.internal.TwitterPullerApplication;

public abstract class BaseActivity extends AppCompatActivity {

  protected static void addFragmentToActivity(@NonNull FragmentManager fragmentManager,
      @NonNull Fragment fragment, @IdRes int containerViewId) {
    FragmentTransaction transaction = fragmentManager.beginTransaction();
    transaction.add(containerViewId, fragment);
    transaction.commit();
  }

  protected ApplicationComponent getApplicationComponent() {
    return TwitterPullerApplication.get(this).getComponent();
  }
}

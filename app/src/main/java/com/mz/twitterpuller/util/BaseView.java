package com.mz.twitterpuller.util;

import android.support.annotation.NonNull;

public interface BaseView<T> {

  void setPresenter(@NonNull T presenter);
}

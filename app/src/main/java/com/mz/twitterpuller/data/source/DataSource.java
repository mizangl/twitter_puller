package com.mz.twitterpuller.data.source;

import android.app.Activity;
import io.reactivex.Observable;

public interface DataSource {

  Observable<Boolean> doLogin(Activity activity);
}

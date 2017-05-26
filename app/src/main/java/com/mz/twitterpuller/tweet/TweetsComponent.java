package com.mz.twitterpuller.tweet;

import com.mz.twitterpuller.internal.ActivityScope;
import com.mz.twitterpuller.internal.ApplicationComponent;
import dagger.Component;

@ActivityScope @Component(dependencies = ApplicationComponent.class, modules = TweetsModule.class)
public interface TweetsComponent {

  void inject(TweetsActivity activity);
}

package com.mz.twitterpuller.login;

import com.mz.twitterpuller.internal.ActivityScope;
import com.mz.twitterpuller.internal.ApplicationComponent;
import dagger.Component;

@ActivityScope @Component(dependencies = ApplicationComponent.class, modules = LoginModule.class)
public interface LoginComponent {
  void inject(LoginActivity activity);
}

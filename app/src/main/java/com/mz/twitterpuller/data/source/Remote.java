package com.mz.twitterpuller.data.source;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.inject.Qualifier;

@Documented @Qualifier @Retention(RetentionPolicy.RUNTIME)
public @interface Remote {
}

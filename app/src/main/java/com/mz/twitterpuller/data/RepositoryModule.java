package com.mz.twitterpuller.data;

import android.content.Context;
import com.mz.twitterpuller.data.model.mapper.TweetModelMapper;
import com.mz.twitterpuller.data.source.DataSource;
import com.mz.twitterpuller.data.source.remote.RemoteTwitterDataSource;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module public class RepositoryModule {

  @Singleton @Provides DataSource provideRemoteDataSource(Context context,
      TweetModelMapper tweetModelMapper) {
    return new RemoteTwitterDataSource(context, tweetModelMapper);
  }
}

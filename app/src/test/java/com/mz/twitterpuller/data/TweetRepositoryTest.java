package com.mz.twitterpuller.data;

import com.mz.twitterpuller.data.model.TweetModel;
import com.mz.twitterpuller.data.source.DataSource;
import io.reactivex.Observable;
import io.reactivex.functions.Predicate;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class) public class TweetRepositoryTest {

  @Mock private DataSource mockLocalDataSource;
  @Mock private DataSource mockRemoteDataSource;

  private TwitterRepository repository;

  @Before public void setUp() {
    repository = new TwitterRepository(mockRemoteDataSource, mockLocalDataSource);
  }

  @Test public void testLocalContactOperator() throws InterruptedException {
    List<TweetModel> localTweets = TweetsUtils.generateList();
    List<TweetModel> remoteTweets = TweetsUtils.generateList();

    Observable<List<TweetModel>> local =
        Observable.just(localTweets).filter(new Predicate<List<TweetModel>>() {
          @Override public boolean test(List<TweetModel> models) throws Exception {
            return true;
          }
        });
    Observable<List<TweetModel>> remote = Observable.just(remoteTweets);

    Observable<List<TweetModel>> listObservable =
        Observable.concat(local, remote).firstElement().toObservable();

    List<TweetModel> models = listObservable.test().values().get(0);

    assertThat(models).hasSameElementsAs(localTweets);
  }

  @Test public void testRemoteContactOperator() throws InterruptedException {
    List<TweetModel> localTweets = TweetsUtils.generateList();
    List<TweetModel> remoteTweets = TweetsUtils.generateList();

    Observable<List<TweetModel>> local =
        Observable.just(localTweets).filter(new Predicate<List<TweetModel>>() {
          @Override public boolean test(List<TweetModel> models) throws Exception {
            return false;
          }
        });
    Observable<List<TweetModel>> remote = Observable.just(remoteTweets);

    Observable<List<TweetModel>> listObservable = Observable.concat(local, remote).firstElement().toObservable();

    List<TweetModel> models = listObservable.test().values().get(0);

    assertThat(models).hasSameElementsAs(remoteTweets);
  }

  @Test public void pullFromLocalTest() throws InterruptedException {
    List<TweetModel> localTweets = TweetsUtils.generateList();
    List<TweetModel> remoteTweets = TweetsUtils.generateList();

    Integer count = 0;
    Long since = null;
    Long max = null;

    addDataToLocalDataSource(localTweets, count, since, max);

    addDataToRemoteDataSource(remoteTweets, count, since, max);

    List<TweetModel> models = repository.pullTweets(count, since, max).test().values().get(0);

    assertThat(models).hasSameElementsAs(localTweets);
  }

  @Test public void pullFromRemoteNewTweetsTest() {
    List<TweetModel> localTweets = TweetsUtils.generateList();
    List<TweetModel> remoteTweets = TweetsUtils.generateList();

    Integer count = 0;
    Long since = 1L;
    Long max = null;

    addDataToLocalDataSource(localTweets, count, since, max);

    addDataToRemoteDataSource(remoteTweets, count, since, max);

    List<TweetModel> models = repository.pullTweets(count, since, max).test().values().get(0);

    assertThat(models).hasSameElementsAs(remoteTweets);
  }

  @Test public void pullFromRemoteOldTweetsTest() {
    List<TweetModel> localTweets = TweetsUtils.generateList();
    List<TweetModel> remoteTweets = TweetsUtils.generateList();

    Integer count = 0;
    Long since = null;
    Long max = 1L;

    addDataToLocalDataSource(localTweets, count, since, max);

    addDataToRemoteDataSource(remoteTweets, count, since, max);

    List<TweetModel> models = repository.pullTweets(count, since, max).test().values().get(0);

    assertThat(models).hasSameElementsAs(remoteTweets);
  }

  @Test public void noDataFromLocalTest() {
    List<TweetModel> remoteTweets = TweetsUtils.generateList();

    Integer count = 0;
    Long since = null;
    Long max = null;

    returnEmptyDataFromLocal(count, since, max);

    addDataToRemoteDataSource(remoteTweets, count, since, max);

    List<TweetModel> models = repository.pullTweets(count, since, max).test().values().get(0);

    assertThat(models).hasSameElementsAs(remoteTweets);
  }

  private void addDataToLocalDataSource(List<TweetModel> tweetModels, Integer count, Long since,
      Long max) {
    when(mockLocalDataSource.pullTweets(count, since, max)).thenReturn(
        Observable.just(tweetModels));
  }

  private void addDataToRemoteDataSource(List<TweetModel> tweetModels, Integer count, Long since,
      Long max) {
    when(mockRemoteDataSource.pullTweets(count, since, max)).thenReturn(
        Observable.just(tweetModels));
  }

  private void returnEmptyDataFromLocal(Integer count, Long since, Long max) {
    when(mockLocalDataSource.pullTweets(count, since, max)).thenReturn(
        Observable.<List<TweetModel>>empty());
  }

  private void returnEmptyDataFromRemote(Integer count, Long since, Long max) {
    when(mockRemoteDataSource.pullTweets(count, since, max)).thenReturn(
        Observable.<List<TweetModel>>empty());
  }
}

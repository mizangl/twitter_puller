package com.mz.twitterpuller.source.local;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import com.mz.twitterpuller.data.TweetsUtils;
import com.mz.twitterpuller.data.model.TweetModel;
import com.mz.twitterpuller.data.model.mapper.TweetModelMapper;
import com.mz.twitterpuller.data.source.local.LocalTwitterDataSource;
import io.reactivex.observers.TestObserver;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.mz.twitterpuller.data.TweetsUtils.createTweet;
import static com.mz.twitterpuller.data.TweetsUtils.generateList;

@RunWith(AndroidJUnit4.class) public class LocalTwitterDataSourceTest {

  private LocalTwitterDataSource localDataSource;

  @Before public void setUp() {
    TweetModelMapper modelMapper = new TweetModelMapper();
    localDataSource =
        new LocalTwitterDataSource(InstrumentationRegistry.getTargetContext(), modelMapper);
  }

  @After public void cleanDb() {
    localDataSource.cleanLocal();
  }

  @Test public void saveLocallyTest() {
    ArrayList<TweetModel> models = generateList();
    TweetModel[] arrayBefore = models.toArray(new TweetModel[models.size()]);

    localDataSource.savedLocally(models);

    List<TweetModel> tweetsFromDB = localDataSource.getTweetsFromDB();

    TweetModel[] arrayAfter = tweetsFromDB.toArray(new TweetModel[tweetsFromDB.size()]);

    Assert.assertFalse(tweetsFromDB.isEmpty());
    Assert.assertArrayEquals(arrayBefore, arrayAfter);
  }

  @Test public void saveAndReplaceSameIdTweets() {
    ArrayList<TweetModel> models = generateList();

    //new tweet
    models.add(createTweet(4, "asd", "no content", "", "123"));

    localDataSource.savedLocally(models);

    List<TweetModel> tweetsFromDB = localDataSource.getTweetsFromDB();

    Assert.assertTrue(tweetsFromDB.size() == 4);

    //repeat one
    TweetModel tweet =
        createTweet(TweetsUtils.ID_1, TweetsUtils.USER_1, "content 1", "https://", "134343233");

    localDataSource.savedLocally(Arrays.asList(tweet));

    tweetsFromDB = localDataSource.getTweetsFromDB();

    Assert.assertTrue(tweetsFromDB.size() == 4);
  }

  @Test public void returnEmptyObservableTest() {
    localDataSource.cleanLocal();

    TestObserver<List<TweetModel>> test = localDataSource.pullTweets(0, null, null).test();
    test.assertNoValues();
  }
}

package com.mz.twitterpuller.source.local;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import com.mz.twitterpuller.data.model.TweetModel;
import com.mz.twitterpuller.data.model.mapper.TweetModelMapper;
import com.mz.twitterpuller.data.source.local.LocalTwitterDataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class) public class LocalTwitterDataSourceTest {

  private static final long ID_1 = 1;
  private static final long ID_2 = 2;
  private static final long ID_3 = 3;

  private static final String USER_1 = "user 1";
  private static final String USER_2 = "user 2";
  private static final String USER_3 = "user 3";

  private LocalTwitterDataSource localDataSource;
  private TweetModelMapper modelMapper;

  @Before public void setUp() {
    modelMapper = new TweetModelMapper();
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

  private ArrayList<TweetModel> generateList() {
    ArrayList<TweetModel> models = new ArrayList<>();

    models.add(createTweet(ID_1, USER_1, "content 1", "https://", "134343233"));
    models.add(createTweet(ID_2, USER_2, "content 2", "https://", "134343233"));
    models.add(createTweet(ID_3, USER_3, "content 3", "https://", "134343233"));

    return models;
  }

  @Test public void saveAndReplaceSameIdTweets() {
    ArrayList<TweetModel> models = generateList();

    //new tweet
    models.add(createTweet(4, "asd", "no content", "", "123"));

    localDataSource.savedLocally(models);

    List<TweetModel> tweetsFromDB = localDataSource.getTweetsFromDB();

    Assert.assertTrue(tweetsFromDB.size() == 4);

    //repeat one
    TweetModel tweet = createTweet(ID_1, USER_1, "content 1", "https://", "134343233");

    localDataSource.savedLocally(Arrays.asList(tweet));

    tweetsFromDB = localDataSource.getTweetsFromDB();

    Assert.assertTrue(tweetsFromDB.size() == 4);
  }

  private TweetModel createTweet(long id, String username, String content, String profile,
      String createdAt) {

    return new TweetModel.Builder().setId(id)
        .setProfile(profile)
        .setContent(content)
        .setUsername(username)
        .setCreateAt(createdAt)
        .build();
  }
}

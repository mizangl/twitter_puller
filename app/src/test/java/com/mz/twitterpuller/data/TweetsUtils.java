package com.mz.twitterpuller.data;

import com.mz.twitterpuller.data.model.TweetModel;
import java.util.ArrayList;
import java.util.List;

public final class TweetsUtils {

  public static final String USER_1 = "user 1";
  public static final String USER_2 = "user 2";
  public static final String USER_3 = "user 3";

  public static List<TweetModel> generateList() {
    ArrayList<TweetModel> models = new ArrayList<>();

    models.add(createTweet((long) (Math.random() * 100), USER_1, "content 1", "https://",
        String.valueOf(System.currentTimeMillis())));
    models.add(createTweet((long) (Math.random() * 100), USER_2, "content 2", "https://",
        String.valueOf(System.currentTimeMillis())));
    models.add(createTweet((long) (Math.random() * 100), USER_3, "content 3", "https://",
        String.valueOf(System.currentTimeMillis())));

    return models;
  }

  public static TweetModel createTweet(long id, String username, String content, String profile,
      String createdAt) {

    return new TweetModel.Builder().setId(id)
        .setProfile(profile)
        .setContent(content)
        .setUsername(username)
        .setCreateAt(createdAt)
        .build();
  }
}

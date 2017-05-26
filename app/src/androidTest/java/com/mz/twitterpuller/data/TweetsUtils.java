package com.mz.twitterpuller.data;

import com.mz.twitterpuller.data.model.TweetModel;
import java.util.ArrayList;

public final class TweetsUtils {

  public static final long ID_1 = 1;
  public static final long ID_2 = 2;
  public static final long ID_3 = 3;

  public static final String USER_1 = "user 1";
  public static final String USER_2 = "user 2";
  public static final String USER_3 = "user 3";

  public static ArrayList<TweetModel> generateList() {
    ArrayList<TweetModel> models = new ArrayList<>();

    models.add(createTweet(ID_1, USER_1, "content 1", "https://", "134343233"));
    models.add(createTweet(ID_2, USER_2, "content 2", "https://", "134343233"));
    models.add(createTweet(ID_3, USER_3, "content 3", "https://", "134343233"));

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

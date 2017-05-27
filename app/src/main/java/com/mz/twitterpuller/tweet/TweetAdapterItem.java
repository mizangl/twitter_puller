package com.mz.twitterpuller.tweet;

interface TweetAdapterItem<Model> {

  int PROGRESS = -1;
  int TWEET_BASIC = 0;
  int TWEET_ONE_IMAGE = 1;
  int TWEET_TWO_IMAGE = 2;
  int TWEET_THREE_IMAGE = 3;
  int TWEET_FOUR_IMAGE = 4;

  int getType();

  Model getModel();
}

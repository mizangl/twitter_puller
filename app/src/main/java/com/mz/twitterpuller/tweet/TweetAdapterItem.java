package com.mz.twitterpuller.tweet;

interface TweetAdapterItem<Model> {

  int PROGRESS = 0;
  int TWEET = 1;

  int getType();

  Model getModel();
}

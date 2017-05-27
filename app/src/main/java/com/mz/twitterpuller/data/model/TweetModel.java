package com.mz.twitterpuller.data.model;

import android.support.annotation.VisibleForTesting;

public class TweetModel {
  public long id;
  public String content;
  public String username;
  public String profile;
  public String createdAt;

  public String[] media;

  public String[] getMedia() {
    if (media == null) return new String[] {};
    return media;
  }

  @Override public String toString() {
    return "TweetModel{"
        + "id="
        + id
        + ", content='"
        + content
        + '\''
        + ", username='"
        + username
        + '\''
        + ", profile='"
        + profile
        + '\''
        + ", createdAt='"
        + createdAt
        + '\''
        + '}';
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    TweetModel model = (TweetModel) o;

    if (id != model.id) return false;
    return createdAt != null ? createdAt.equals(model.createdAt) : model.createdAt == null;
  }

  @Override public int hashCode() {
    int result = (int) (id ^ (id >>> 32));
    result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
    return result;
  }

  @VisibleForTesting public static class Builder {

    private final TweetModel model;

    public Builder() {
      model = new TweetModel();
    }

    public Builder setId(long id) {
      model.id = id;
      return this;
    }

    public Builder setContent(String content) {
      model.content = content;
      return this;
    }

    public Builder setUsername(String username) {
      model.username = username;
      return this;
    }

    public Builder setProfile(String profile) {
      model.profile = profile;
      return this;
    }

    public Builder setCreateAt(String createAt) {
      model.createdAt = createAt;
      return this;
    }

    public TweetModel build() {
      return model;
    }
  }
}

package io.resourcepool.dashboard.model.metadata;

import java.util.List;

/**
 * TODO class details.
 *
 * @author Loïc Ortola on 03/03/2017
 */
public class Feed {
  private String uuid;
  private String name;
  private List<String> bundleTags;

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<String> getBundleTags() {
    return bundleTags;
  }

  public void setBundleTags(List<String> bundleTags) {
    this.bundleTags = bundleTags;
  }


  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private String uuid;
    private String name;
    private List<String> bundles;

    private Builder() {
    }

    public Builder uuid(String uuid) {
      this.uuid = uuid;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder bundles(List<String> bundles) {
      this.bundles = bundles;
      return this;
    }

    public Feed build() {
      Feed feed = new Feed();
      feed.setUuid(uuid);
      feed.setName(name);
      feed.setBundleTags(bundles);
      return feed;
    }
  }
}

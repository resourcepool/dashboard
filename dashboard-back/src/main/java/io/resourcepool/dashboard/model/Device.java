package io.resourcepool.dashboard.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.resourcepool.dashboard.dao.serialization.LocalDateTimeDeserializer;
import io.resourcepool.dashboard.dao.serialization.LocalDateTimeSerializer;

import java.time.LocalDateTime;

/**
 * TODO class details.
 *
 * @author Loïc Ortola on 03/03/2017
 */
public class Device {

  private String id;
  private String feedId;
  private String name;
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private LocalDateTime lastHealthCheck;
  private String lastKnownIp;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getFeedId() {
    return feedId;
  }

  public void setFeedId(String feedId) {
    this.feedId = feedId;
  }

  public LocalDateTime getLastHealthCheck() {
    return lastHealthCheck;
  }

  public void setLastHealthCheck(LocalDateTime lastHealthCheck) {
    this.lastHealthCheck = lastHealthCheck;
  }

  public String getLastKnownIp() {
    return lastKnownIp;
  }

  public void setLastKnownIp(String lastKnownIp) {
    this.lastKnownIp = lastKnownIp;
  }


  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private String id;
    private String feedId;
    private String name;
    private LocalDateTime lastHealthCheck;
    private String lastKnownIp;

    private Builder() {
    }

    public Builder id(String id) {
      this.id = id;
      return this;
    }

    public Builder feedId(String feedId) {
      this.feedId = feedId;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder lastHealthCheck(LocalDateTime lastHealthCheck) {
      this.lastHealthCheck = lastHealthCheck;
      return this;
    }

    public Builder lastKnownIp(String lastKnownIp) {
      this.lastKnownIp = lastKnownIp;
      return this;
    }

    public Device build() {
      Device device = new Device();
      device.setId(id);
      device.setFeedId(feedId);
      device.setName(name);
      device.setLastHealthCheck(lastHealthCheck);
      device.setLastKnownIp(lastKnownIp);
      return device;
    }
  }
}

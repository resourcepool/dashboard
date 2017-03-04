package io.resourcepool.dashboard.rest.dtos;

/**
 * Created by loicortola on 04/03/2017.
 */
public class Device {

    private String id;
    private String feedId;
    private String name;

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

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String id;
        private String feedId;
        private String name;
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

        public Builder lastKnownIp(String lastKnownIp) {
            this.lastKnownIp = lastKnownIp;
            return this;
        }

        public Device build() {
            Device device = new Device();
            device.setId(id);
            device.setFeedId(feedId);
            device.setName(name);
            return device;
        }
    }
}

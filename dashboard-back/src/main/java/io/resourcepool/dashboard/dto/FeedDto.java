package io.resourcepool.dashboard.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class FeedDto {

    private String uuid;

    private String name;

    private List<String> bundleTags;

    @SuppressWarnings("unused")
    public FeedDto() {
    }

    private FeedDto(String uuid, String name, List<String> bundleTags) {
        this.uuid = uuid;
        this.name = name;
        this.bundleTags = bundleTags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FeedDto feedDto = (FeedDto) o;

        if (uuid != null ? !uuid.equals(feedDto.uuid) : feedDto.uuid != null) return false;
        if (name != null ? !name.equals(feedDto.name) : feedDto.name != null) return false;
        return bundleTags != null ? bundleTags.equals(feedDto.bundleTags) : feedDto.bundleTags == null;
    }

    @Override
    public int hashCode() {
        int result = uuid != null ? uuid.hashCode() : 0;
        return result;
    }

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
        private List<String> bundleTags;

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

        public Builder bundleTags(List<String> bundleTags) {
            this.bundleTags = bundleTags;
            return this;
        }

        public FeedDto build() {
            FeedDto feedDto = new FeedDto(uuid, name, bundleTags);
            return feedDto;
        }
    }
}

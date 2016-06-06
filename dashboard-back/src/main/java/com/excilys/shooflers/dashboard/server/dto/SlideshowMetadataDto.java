package com.excilys.shooflers.dashboard.server.dto;


import java.util.ArrayList;
import java.util.List;

public class SlideshowMetadataDto {

    private String uuid;

    private String name;

    private String start;

    private String end;

    private List<MediaMetadataDto> medias;

    private SlideshowMetadataDto(String uuid, String name, ValidityDto validityDto, List<MediaMetadataDto> medias) {
        this.uuid = uuid;
        this.name = name;
        this.start = validityDto.getStart();
        this.end = validityDto.getEnd();
        this.medias = medias;
    }

    public static class Builder {
        private String name;

        private ValidityDto validityDto;

        private List<MediaMetadataDto> medias;

        private String uuid;

        public Builder uuid(String uuid) {
            this.uuid = uuid;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder validity(ValidityDto validityDto) {
            this.validityDto = validityDto;
            return this;
        }

        public Builder medias(List<MediaMetadataDto> medias) {
            this.medias = medias;
            return this;
        }

        public SlideshowMetadataDto build() {
            if (medias == null) {
                medias = new ArrayList<>();
            }
            return new SlideshowMetadataDto(uuid, name, validityDto, medias);
        }
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

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public List<MediaMetadataDto> getMedias() {
        return medias;
    }

    public void setMedias(List<MediaMetadataDto> medias) {
        this.medias = medias;
    }
}

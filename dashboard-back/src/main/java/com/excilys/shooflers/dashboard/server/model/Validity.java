package com.excilys.shooflers.dashboard.server.model;

import com.excilys.shooflers.dashboard.server.dao.serialization.LocalDateTimeDeserializer;
import com.excilys.shooflers.dashboard.server.dao.serialization.LocalDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.LocalDateTime;

/**
 * Period of validity of a media or a slideshow. If start and end are not defined,
 * the period of validity is 24 hours from the creation of the object.
 */
public class Validity {

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime start;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime end;

    public Validity() { }

    private Validity(LocalDateTime start, LocalDateTime end) {
        this.start = start;
        this.end = end;
    }

    public static class Builder {
        private LocalDateTime start;

        private LocalDateTime end;

        public Builder start(LocalDateTime start) {
            this.start = start;
            return this;
        }

        public Builder end(LocalDateTime end) {
            this.end = end;
            return this;
        }

        public Validity build() {
            if (start == null) {
                start = LocalDateTime.now();
            }

            return new Validity(start, end);
        }
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }
}

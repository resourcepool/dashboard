package com.excilys.shooflers.dashboard.server.model;

import java.time.LocalDateTime;

/**
 * Period of validity of a media or a slideshow. If start and end are not defined,
 * the period of validity is 24 hours from the creation of the object.
 */
public class Validity {

    private LocalDateTime start;

    private LocalDateTime end;

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

            if (end == null) {
                end = start.plusDays(1);
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

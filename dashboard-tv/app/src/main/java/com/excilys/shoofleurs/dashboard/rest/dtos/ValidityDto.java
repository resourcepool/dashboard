package com.excilys.shoofleurs.dashboard.rest.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by excilys on 09/06/16.
 */
public class ValidityDto {
    @JsonProperty("start")
    private String mStart;

    @JsonProperty("end")
    private String mEnd;

    public String getStart() {
        return mStart;
    }

    public void setStart(String start) {
        mStart = start;
    }

    public String getEnd() {
        return mEnd;
    }

    public void setEnd(String end) {
        mEnd = end;
    }
}

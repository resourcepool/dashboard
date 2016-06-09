package com.excilys.shoofleurs.dashboard.rest.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by excilys on 09/06/16.
 */
public class ValidityDto {
    @JsonProperty("start")
    private String mStartTime;

    @JsonProperty("end")
    private String mEndTime;

    public String getStartTime() {
        return mStartTime;
    }

    public void setStartTime(String startTime) {
        mStartTime = startTime;
    }

    public String getEndTime() {
        return mEndTime;
    }

    public void setEndTime(String endTime) {
        mEndTime = endTime;
    }
}

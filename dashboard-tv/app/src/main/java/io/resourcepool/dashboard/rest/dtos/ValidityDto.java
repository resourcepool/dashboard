package io.resourcepool.dashboard.rest.dtos;

import com.google.gson.annotations.SerializedName;


public class ValidityDto {
    @SerializedName("start")
    private String mStart;

    @SerializedName("end")
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

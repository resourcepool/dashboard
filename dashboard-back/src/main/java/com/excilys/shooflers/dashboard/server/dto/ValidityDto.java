package com.excilys.shooflers.dashboard.server.dto;


public class ValidityDto {

    private String start;

    private String end;

    public ValidityDto() { }

    public ValidityDto(String start, String end) {
        this.start = start;
        this.end = end;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ValidityDto)) return false;

        ValidityDto that = (ValidityDto) o;

        if (start != null ? !start.equals(that.start) : that.start != null) return false;
        return end != null ? end.equals(that.end) : that.end == null;

    }
}

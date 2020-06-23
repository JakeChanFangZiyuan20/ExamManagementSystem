package com.cyj.ems.utils;

public class ArrangeClr {

    private String clrNumber;
    private Integer capacity;
    private Integer startPosition;
    private Integer endPosition;
    private Integer extra;

    @Override
    public String toString() {
        return "ArrangeClr{" +
                "clrNumber='" + clrNumber + '\'' +
                ", capacity=" + capacity +
                ", startPosition=" + startPosition +
                ", endPosition=" + endPosition +
                ", extra=" + extra +
                '}';
    }

    public String getClrNumber() {
        return clrNumber;
    }

    public void setClrNumber(String clrNumber) {
        this.clrNumber = clrNumber;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(Integer startPosition) {
        this.startPosition = startPosition;
    }

    public Integer getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(Integer endPosition) {
        this.endPosition = endPosition;
    }

    public Integer getExtra() {
        return extra;
    }

    public void setExtra(Integer extra) {
        this.extra = extra;
    }
}

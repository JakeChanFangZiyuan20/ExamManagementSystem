package com.cyj.ems.utils;

import java.util.List;

public class ArrangeClr2 {

    private String clrNumber;
    private Integer capacity;
    private Integer examineeNumber;

    @Override
    public String toString() {
        return "ArrangeClr2{" +
                "clrNumber='" + clrNumber + '\'' +
                ", capacity=" + capacity +
                ", examineeNumber=" + examineeNumber +
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

    public Integer getExamineeNumber() {
        return examineeNumber;
    }

    public void setExamineeNumber(Integer examineeNumber) {
        this.examineeNumber = examineeNumber;
    }
}

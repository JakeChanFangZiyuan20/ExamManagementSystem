package com.cyj.ems.utils;

import java.io.Serializable;
import java.util.Set;

public class ExamClrInfo implements Serializable {

    private String address;
    private String clrNumber;
    private Integer capacity;
    private Integer canContain;
    private Set<Integer> canSitSet;

    @Override
    public String toString() {
        return "ExamClrInfo{" +
                "address='" + address + '\'' +
                ", clrNumber='" + clrNumber + '\'' +
                ", capacity=" + capacity +
                ", canContain=" + canContain +
                ", canSitSet=" + canSitSet +
                '}';
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public Integer getCanContain() {
        return canContain;
    }

    public void setCanContain(Integer canContain) {
        this.canContain = canContain;
    }

    public Set<Integer> getCanSitSet() {
        return canSitSet;
    }

    public void setCanSitSet(Set<Integer> canSitSet) {
        this.canSitSet = canSitSet;
    }
}

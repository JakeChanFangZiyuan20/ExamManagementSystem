package com.cyj.ems.domain;

import java.io.Serializable;

public class Classroom implements Serializable {

    private Integer clrID;
    private String clrNumber;
    private String address;
    private Integer capacity;

    @Override
    public String toString() {
        return "Classroom{" +
                "clrID=" + clrID +
                ", clrNumber='" + clrNumber + '\'' +
                ", address='" + address + '\'' +
                ", capacity=" + capacity +
                '}';
    }

    public Integer getClrID() {
        return clrID;
    }

    public void setClrID(Integer clrID) {
        this.clrID = clrID;
    }

    public String getClrNumber() {
        return clrNumber;
    }

    public void setClrNumber(String clrNumber) {
        this.clrNumber = clrNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
}

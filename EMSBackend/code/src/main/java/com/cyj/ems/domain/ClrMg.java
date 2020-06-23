package com.cyj.ems.domain;

import java.io.Serializable;

public class ClrMg implements Serializable {

    private Integer clrID;
    private String mgName;
    private String mgPhone;

    @Override
    public String toString() {
        return "ClrMg{" +
                "clrID=" + clrID +
                ", mgName='" + mgName + '\'' +
                ", mgPhone='" + mgPhone + '\'' +
                '}';
    }

    public Integer getClrID() {
        return clrID;
    }

    public void setClrID(Integer clrID) {
        this.clrID = clrID;
    }

    public String getMgName() {
        return mgName;
    }

    public void setMgName(String mgName) {
        this.mgName = mgName;
    }

    public String getMgPhone() {
        return mgPhone;
    }

    public void setMgPhone(String mgPhone) {
        this.mgPhone = mgPhone;
    }
}

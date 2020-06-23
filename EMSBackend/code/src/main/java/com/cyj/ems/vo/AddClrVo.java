package com.cyj.ems.vo;

public class AddClrVo {

    private String sessionID;
    private String userName;
    private String clrNumber;
    private String address;
    private Integer capacity;

    @Override
    public String toString() {
        return "AddClrVo{" +
                "sessionID='" + sessionID + '\'' +
                ", userName='" + userName + '\'' +
                ", clrNumber='" + clrNumber + '\'' +
                ", address='" + address + '\'' +
                ", capacity=" + capacity +
                '}';
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

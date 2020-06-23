package com.cyj.ems.vo;

public class NormalRequestVO {

    private String sessionID;
    private String userName;

    @Override
    public String toString() {
        return "NormalRequestVO{" +
                "sessionID='" + sessionID + '\'' +
                ", userName='" + userName + '\'' +
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
}

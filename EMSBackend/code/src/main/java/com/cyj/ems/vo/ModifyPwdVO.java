package com.cyj.ems.vo;

import java.io.Serializable;

public class ModifyPwdVO implements Serializable {

    private String sessionID;
    private String userName;
    private String pwd;

    @Override
    public String toString() {
        return "ModifyPwdVO{" +
                "sessionID='" + sessionID + '\'' +
                ", userName='" + userName + '\'' +
                ", pwd='" + pwd + '\'' +
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

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}

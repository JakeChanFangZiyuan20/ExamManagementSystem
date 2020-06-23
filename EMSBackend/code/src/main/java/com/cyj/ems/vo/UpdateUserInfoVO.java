package com.cyj.ems.vo;

import java.io.Serializable;
import java.sql.Date;

public class UpdateUserInfoVO implements Serializable {

    private String sessionID;
    private String userName;
    private String trueName;
    private String gender;
    private Date birthday;
    private String identity;
    private String phone;
    private String email;

    @Override
    public String toString() {
        return "UpdateUserInfoVO{" +
                "sessionID='" + sessionID + '\'' +
                ", userName='" + userName + '\'' +
                ", trueName='" + trueName + '\'' +
                ", gender='" + gender + '\'' +
                ", birthday=" + birthday +
                ", identity='" + identity + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
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

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

package com.cyj.ems.dto;

import java.io.Serializable;
import java.sql.Date;

/**
 * 客户端 sessionStorage 中保存的内容
 */
public class UserInfoDTO implements Serializable {

    private String sessionID;
    private String userName;
    private Integer role;
    private String trueName;
    private String gender;
    private Date birthday;
    private String identity; // 身份证
    private String phone;
    private String email;
    private String photoURL;

    @Override
    public String toString() {
        return "UserInfoDTO{" +
                "sessionID='" + sessionID + '\'' +
                ", userName='" + userName + '\'' +
                ", role=" + role +
                ", trueName='" + trueName + '\'' +
                ", gender='" + gender + '\'' +
                ", birthday=" + birthday +
                ", identity='" + identity + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", photoURL='" + photoURL + '\'' +
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

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
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

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }
}

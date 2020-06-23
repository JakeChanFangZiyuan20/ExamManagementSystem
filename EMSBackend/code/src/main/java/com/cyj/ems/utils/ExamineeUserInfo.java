package com.cyj.ems.utils;

import java.io.Serializable;
import java.sql.Date;

public class ExamineeUserInfo implements Serializable {

    private String examineeNumber;
    private String userName;
    private String trueName;
    private String identity;
    private Integer paymentStatus;
    private String clrNumber;
    private Integer seatNumber;
    private String gender;
    private Date birthday;
    private String phone;

    @Override
    public String toString() {
        return "ExamineeUserInfo{" +
                "examineeNumber='" + examineeNumber + '\'' +
                ", userName='" + userName + '\'' +
                ", trueName='" + trueName + '\'' +
                ", identity='" + identity + '\'' +
                ", paymentStatus=" + paymentStatus +
                ", clrNumber='" + clrNumber + '\'' +
                ", seatNumber=" + seatNumber +
                ", gender='" + gender + '\'' +
                ", birthday=" + birthday +
                ", phone='" + phone + '\'' +
                '}';
    }

    public String getExamineeNumber() {
        return examineeNumber;
    }

    public void setExamineeNumber(String examineeNumber) {
        this.examineeNumber = examineeNumber;
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

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public Integer getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(Integer paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getClrNumber() {
        return clrNumber;
    }

    public void setClrNumber(String clrNumber) {
        this.clrNumber = clrNumber;
    }

    public Integer getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(Integer seatNumber) {
        this.seatNumber = seatNumber;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

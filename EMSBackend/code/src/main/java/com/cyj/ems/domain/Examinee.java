package com.cyj.ems.domain;

import java.io.Serializable;

public class Examinee implements Serializable {

    private Integer examineeID;
    private String examineeNumber; // 考生准考证号
    private String userName; // 考生用户名
    private String examNumber;  // 考试编号
    private Integer paymentStatus; // 付款状态，0 未付款，1 已付款
    private String clrNumber;
    private Integer seatNumber; // 座位号

    @Override
    public String toString() {
        return "Examinee{" +
                "examineeID=" + examineeID +
                ", examineeNumber='" + examineeNumber + '\'' +
                ", userName='" + userName + '\'' +
                ", examNumber='" + examNumber + '\'' +
                ", paymentStatus=" + paymentStatus +
                ", clrNumber='" + clrNumber + '\'' +
                ", seatNumber=" + seatNumber +
                '}';
    }

    public Integer getExamineeID() {
        return examineeID;
    }

    public void setExamineeID(Integer examineeID) {
        this.examineeID = examineeID;
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

    public String getExamNumber() {
        return examNumber;
    }

    public void setExamNumber(String examNumber) {
        this.examNumber = examNumber;
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
}

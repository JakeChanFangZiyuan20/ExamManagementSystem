package com.cyj.ems.utils;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;

public class ExamExaminee implements Serializable {

    private String examNumber;
    private String examName;
    private Date examDate;
    private Time startTime;
    private Time endTime;
    private Integer regPrice;
    private String examineeNumber;
    private Integer paymentStatus;
    private String clrNumber;
    private Integer seatNumber;

    @Override
    public String toString() {
        return "ExamExaminee{" +
                "examNumber='" + examNumber + '\'' +
                ", examName='" + examName + '\'' +
                ", examDate=" + examDate +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", regPrice=" + regPrice +
                ", examineeNumber='" + examineeNumber + '\'' +
                ", paymentStatus=" + paymentStatus +
                ", clrNumber='" + clrNumber + '\'' +
                ", seatNumber=" + seatNumber +
                '}';
    }

    public String getExamNumber() {
        return examNumber;
    }

    public void setExamNumber(String examNumber) {
        this.examNumber = examNumber;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public Date getExamDate() {
        return examDate;
    }

    public void setExamDate(Date examDate) {
        this.examDate = examDate;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public Integer getRegPrice() {
        return regPrice;
    }

    public void setRegPrice(Integer regPrice) {
        this.regPrice = regPrice;
    }

    public String getExamineeNumber() {
        return examineeNumber;
    }

    public void setExamineeNumber(String examineeNumber) {
        this.examineeNumber = examineeNumber;
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

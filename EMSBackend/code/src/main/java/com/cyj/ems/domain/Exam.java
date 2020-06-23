package com.cyj.ems.domain;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;

public class Exam implements Serializable {

    private Integer examID;
    private String examNumber;
    private String examName;
    private Date examDate;
    private Time startTime;
    private Time endTime;
    private Integer regPrice;

    @Override
    public String toString() {
        return "Exam{" +
                "examID=" + examID +
                ", examNumber='" + examNumber + '\'' +
                ", examName='" + examName + '\'' +
                ", examDate=" + examDate +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", regPrice=" + regPrice +
                '}';
    }

    public Integer getExamID() {
        return examID;
    }

    public void setExamID(Integer examID) {
        this.examID = examID;
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
}

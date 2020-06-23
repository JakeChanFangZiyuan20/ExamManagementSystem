package com.cyj.ems.domain;

import java.io.Serializable;

public class ExamMg implements Serializable {

    private String examNumber;
    private String mgName;
    private String mgPhone;

    @Override
    public String toString() {
        return "ExamMg{" +
                "examNumber='" + examNumber + '\'' +
                ", mgName='" + mgName + '\'' +
                ", mgPhone='" + mgPhone + '\'' +
                '}';
    }

    public String getExamNumber() {
        return examNumber;
    }

    public void setExamNumber(String examNumber) {
        this.examNumber = examNumber;
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

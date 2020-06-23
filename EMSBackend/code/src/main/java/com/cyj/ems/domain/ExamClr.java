package com.cyj.ems.domain;

import java.io.Serializable;

public class ExamClr implements Serializable {

    private String examNumber;
    private String clrNumber;

    @Override
    public String toString() {
        return "ExamClr{" +
                "examNumber='" + examNumber + '\'' +
                ", clrNumber='" + clrNumber + '\'' +
                '}';
    }

    public String getExamNumber() {
        return examNumber;
    }

    public void setExamNumber(String examNumber) {
        this.examNumber = examNumber;
    }

    public String getClrNumber() {
        return clrNumber;
    }

    public void setClrNumber(String clrNumber) {
        this.clrNumber = clrNumber;
    }
}

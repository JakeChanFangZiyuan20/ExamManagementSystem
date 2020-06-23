package com.cyj.ems.dao;

import com.cyj.ems.domain.ExamClr;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamClrDao {

    List<ExamClr> getExamClrListByClrID(String clrNumber);

    void insertExamClrList(@Param("examClrList") List<ExamClr> examClrList);

    List<ExamClr> getExamClrListByExamNumber(String examNumber);

    Integer countExamClrByExamNumber(String examNumber);
}

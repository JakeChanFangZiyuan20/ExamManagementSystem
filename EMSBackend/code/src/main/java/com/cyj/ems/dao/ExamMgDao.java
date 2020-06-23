package com.cyj.ems.dao;

import com.cyj.ems.domain.ExamMg;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamMgDao {

    List<ExamMg> getExamMgListByExamNumber(String examNumber);

    void insertExamMgList (@Param("examMgList") List<ExamMg> examMgList);

    Integer countExamMgByExamNumber(String examNumber);
}

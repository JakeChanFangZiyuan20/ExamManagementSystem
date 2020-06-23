package com.cyj.ems.dao;

import com.cyj.ems.domain.Exam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamDao {

    List<Exam> getAllExam();

    List<Exam> getExamByExamName(String examName);

    List<Exam> getExamListByExamNumberList(@Param("examNumberList") List<String> examNumberList);

    Integer countExam();

    Integer countExamSearchByExamName(String examName);

    Exam getExamByExamID(Integer examID);

    Exam getExamByExamNumber(String examNumber);

    void insertExam(Exam exam);
}

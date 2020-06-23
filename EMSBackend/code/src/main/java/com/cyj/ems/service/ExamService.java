package com.cyj.ems.service;

import com.cyj.ems.dao.ExamDao;
import com.cyj.ems.domain.Exam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("examService")
public class ExamService {

    @Autowired
    ExamDao examDao;

    public List<Exam> getAllExam() {
        return examDao.getAllExam();
    }

    public List<Exam> getExamByExamName(String examName) { // 模糊查询
        return examDao.getExamByExamName("%" + examName + "%");
    }

    public List<Exam> getExamListByExamNumberList(List<String> examNumberList) {
        return examDao.getExamListByExamNumberList(examNumberList);
    }

    public Integer countExamSearchByExamName(String examName) {
        return examDao.countExamSearchByExamName("%" + examName + "%");
    }

    public Integer countExam() {
        return examDao.countExam();
    }

    public Exam getExamByExamID(Integer examID) {
        return examDao.getExamByExamID(examID);
    }

    public Exam getExamByExamNumber(String examNumber) {
        return examDao.getExamByExamNumber(examNumber);
    }

    public boolean hasExam(Exam exam) {
        return examDao.getExamByExamNumber(exam.getExamNumber()) != null;
    }

    public void insertExam(Exam exam) {
        examDao.insertExam(exam);
    }
}

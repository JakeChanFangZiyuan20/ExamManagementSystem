package com.cyj.ems.controller;

import com.cyj.ems.domain.Exam;
import com.cyj.ems.domain.Examinee;
import com.cyj.ems.service.ExamService;
import com.cyj.ems.service.ExamineeService;
import com.cyj.ems.utils.ExamExaminee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ViewExamInfoController {

    // searchExamList is in ViewExamineeController.java

    // build buildExamInfoList is in viewExamineeController.java

    @Autowired
    ExamService examService;

    @Autowired
    ExamineeService examineeService;

    // 打印准考证
    @CrossOrigin
    @PostMapping("/api/viewCertification") // need filter and interceptor
    @ResponseBody
    public Map<String, Object> viewCertification(@RequestBody Map<String, Object> requestMap) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("status", 200);

        String examineeNumber = requestMap.get("examineeNumber").toString();
        List<Examinee> examineeList = examineeService.getExamineeByExamineeNumber(examineeNumber);
        if(examineeList.size() == 0) {
            responseMap.put("status", 400);
            return responseMap;
        }
        List<String> examNumber = new ArrayList<>();
        for(Examinee examinee : examineeList) {
            examNumber.add(examinee.getExamNumber());
        }
        List<Exam> examList = examService.getExamListByExamNumberList(examNumber);

        List<ExamExaminee> examExamineeList = new ArrayList<>();
        for(int i = 0; i < examList.size(); i++) {
            Exam curExam = examList.get(i);
            Examinee curExaminee = examineeList.get(i);
            ExamExaminee newExamExaminee = new ExamExaminee();

            newExamExaminee.setExamNumber(curExam.getExamNumber());
            newExamExaminee.setExamName(curExam.getExamName());
            newExamExaminee.setExamDate(curExam.getExamDate());
            newExamExaminee.setStartTime(curExam.getStartTime());
            newExamExaminee.setEndTime(curExam.getEndTime());
            newExamExaminee.setClrNumber(curExaminee.getClrNumber());
            newExamExaminee.setSeatNumber(curExaminee.getSeatNumber());

            examExamineeList.add(newExamExaminee);
        }

        responseMap.put("examInfoList", examExamineeList);

        return responseMap;
    }
}

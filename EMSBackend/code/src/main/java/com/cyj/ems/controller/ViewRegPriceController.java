package com.cyj.ems.controller;

import com.cyj.ems.domain.Exam;
import com.cyj.ems.domain.Examinee;
import com.cyj.ems.service.ExamService;
import com.cyj.ems.service.ExamineeService;
import com.github.pagehelper.PageHelper;
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
public class ViewRegPriceController {

    @Autowired
    ExamService examService;

    @Autowired
    ExamineeService examineeService;

    @CrossOrigin
    @PostMapping("/api/searchExamFromViewRegPrice") // need filter and interceptor
    @ResponseBody
    public Map<String, Object> searchExam(@RequestBody Map<String, Object> requestMap) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("status", 200);

        String userName = requestMap.get("userName").toString();
        Integer curPage = (Integer) requestMap.get("curPage");
        Integer examTotal = examineeService.countExamineeNonPay(userName);
        if (examTotal == 0) {
            responseMap.put("status", 400);
            return responseMap;
        }
        responseMap.put("examTotal", examTotal);

        PageHelper.startPage(curPage, 6);
        List<Examinee> examineeList = examineeService.getExamineeListByNoPay(userName);
        List<String> examNumberList = new ArrayList<>();
        for (Examinee examinee : examineeList) {
            examNumberList.add(examinee.getExamNumber());
        }
        List<Exam> examList = examService.getExamListByExamNumberList(examNumberList);
        responseMap.put("examList", examList);

        return responseMap;
    }

}

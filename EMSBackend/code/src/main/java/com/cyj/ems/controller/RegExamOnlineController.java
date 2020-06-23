package com.cyj.ems.controller;

import com.alibaba.fastjson.JSON;
import com.cyj.ems.domain.Classroom;
import com.cyj.ems.domain.ExamClr;
import com.cyj.ems.domain.Examinee;
import com.cyj.ems.domain.User;
import com.cyj.ems.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class RegExamOnlineController {

    @Autowired
    UserService userService;

    @Autowired
    ExamineeService examineeService;

    @Autowired
    ExamService examService;

    @Autowired
    ClrService clrService;

    @Autowired
    ExamClrService examClrService;

    @Resource
    RedisTemplate<String, Object> redisTemplate;


    @CrossOrigin
    @PostMapping("/api/checkInfoComplete") // need filter and interceptor
    @ResponseBody
    public Map<String, Object> checkInfoComplete(@RequestBody Map<String, Object> requestMap) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("status", 200);
        responseMap.put("isComplete", true);

        String userName = requestMap.get("userName").toString();
        User user = userService.getUserByUserName(userName);
        if (user.getTrueName() == null || user.getTrueName().equals("")) {
            responseMap.put("isComplete", false);
        } else if (user.getGender() == null || user.getGender().equals("")) {
            responseMap.put("isComplete", false);
        } else if (user.getBirthday() == null) {
            responseMap.put("isComplete", false);
        } else if (user.getIdentity() == null || user.getIdentity().equals("")) {
            responseMap.put("isComplete", false);
        } else if (user.getPhone() == null || user.getPhone().equals("")) {
            responseMap.put("isComplete", false);
        } else if (user.getEmail() == null || user.getEmail().equals("")) {
            responseMap.put("isComplete", false);
        } else if (user.getPhotoURL() == null || user.getPhotoURL().equals("")) {
            responseMap.put("isComplete", false);
        }

        return responseMap;
    }

    @CrossOrigin
    @PostMapping("/api/isAttended") // need filter and interceptor
    @ResponseBody
    public Map<String, Object> isAttended(@RequestBody Map<String, Object> requestMap) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("status", 200);
        responseMap.put("isAttended", false);

        String userName = requestMap.get("userName").toString();
        String examNumber = requestMap.get("examNumber").toString();
        Examinee examinee = examineeService.getExamineeByUserNameAndExamNumber(userName, examNumber);
        if (examinee != null) {
            responseMap.put("isAttended", true);
        }

        return responseMap;
    }

    @CrossOrigin
    @PostMapping("/api/processAttend") // need filter and interceptor
    @ResponseBody
    public Map<String, Object> processAttend(@RequestBody Map<String, Object> requestMap, HttpServletRequest request) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("status", 200);

        Examinee examinee = setExaminee(requestMap);
        if(examinee == null) {
            responseMap.put("status", 402); // 教室无空位
            return responseMap;
        }

        HttpSession session = request.getSession();
        String regExamineeOnlineSessionID = session.getId();
        responseMap.put("regExamineeOnlineSessionID", regExamineeOnlineSessionID);
        session.setAttribute(regExamineeOnlineSessionID, JSON.toJSONString(examinee));

        return responseMap;
    }

    private Examinee setExaminee(Map<String, Object> requestMap) {
        Examinee examinee = new Examinee();
        String randomClrNumber = randomFindClrNumber(requestMap.get("examNumber").toString());
        if(randomClrNumber.equals("")) {
            return null;
        }
        examinee.setClrNumber(randomClrNumber);
        examinee.setSeatNumber(randomFindSeatInClr(requestMap.get("examNumber").toString(), randomClrNumber));

        examinee.setUserName(requestMap.get("userName").toString());
        examinee.setExamNumber(requestMap.get("examNumber").toString());
        examinee.setExamineeNumber(requestMap.get("examineeNumber").toString());
        examinee.setPaymentStatus(0);
        return examinee;
    }

    private String randomFindClrNumber(String examNumber) {
        List<ExamClr> examClrList = examClrService.getExamClrListByExamNumber(examNumber);
        List<String> clrNumberList = new ArrayList<>();
        for(ExamClr examClr : examClrList) {
            clrNumberList.add(examClr.getClrNumber());
        }

        List<String> haveSeatClrNumber = new ArrayList<>();
        for(String clrNumber : clrNumberList) {
            Classroom curClr = clrService.getClassroomByClrNumber(clrNumber);
            Integer haveArranged = examineeService.countExamineeByExamNumberAndClrNumber(examNumber, clrNumber);
            if(curClr.getCapacity() - haveArranged > 0) {
                haveSeatClrNumber.add(clrNumber);
            }
        }

        if(haveSeatClrNumber.size() == 0) {
            return "";
        }

        Random random = new Random();
        Integer randomNO = random.nextInt(haveSeatClrNumber.size());
        return haveSeatClrNumber.get(randomNO);
    }

    private Integer randomFindSeatInClr(String examNumber, String clrNumber) {
        Classroom clr = clrService.getClassroomByClrNumber(clrNumber);
        Set<Integer> takeSet = new HashSet<>(examineeService.getSeatListByExamNumberAndClrNumber(examNumber, clrNumber));
        List<Integer> seatList = new ArrayList<>();
        for(int i = 1; i <= clr.getCapacity(); i++) {
            if(!takeSet.contains(i)){
                seatList.add(i);
            }
        }

        Random random = new Random();
        Integer randomNO = random.nextInt(seatList.size());
        return seatList.get(randomNO);
    }

}

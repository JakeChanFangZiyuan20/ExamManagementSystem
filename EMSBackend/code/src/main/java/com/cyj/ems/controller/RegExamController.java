package com.cyj.ems.controller;

import com.alibaba.fastjson.JSON;
import com.cyj.ems.domain.*;
import com.cyj.ems.service.*;
import com.cyj.ems.utils.ExamClrInfo;
import com.cyj.ems.utils.RegExaminee;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class RegExamController {

    @Value(("${localfilepath.images.temp}"))
    private String imagesTempPath;

    @Autowired
    UserService userService;

    @Autowired
    ExamService examService;

    @Autowired
    ClrService clrService;

    @Autowired
    ExamClrService examClrService;

    @Autowired
    ExamineeService examineeService;

    @Resource
    RedisTemplate<String, Object> redisTemplate;

    @CrossOrigin
    @PostMapping("/api/locateExam") // need filter and interceptor
    @ResponseBody
    public Map<String, Object> locateExam(@RequestBody Map<String, Object> requestMap) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("status", 200);

        String examNumber = requestMap.get("examNumber").toString();
        Exam exam = examService.getExamByExamNumber(examNumber);
        if(exam == null) {
            responseMap.put("status", 400);
            return responseMap;
        }

        responseMap.put("examInfo", exam);

        return responseMap;
    }

    @CrossOrigin
    @PostMapping("/api/getExamClrList") // need filter and interceptor
    @ResponseBody
    public Map<String, Object> getExamClrList(@RequestBody Map<String, Object> requestMap) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("status", 200);

        String examNumber = requestMap.get("examNumber").toString();
        Integer curPage = (Integer) requestMap.get("curPage");

        Integer examClrListTotal = examClrService.countExamClrByExamNumber(examNumber);
        if(examClrListTotal == 0) {
            responseMap.put("status", 400);
            return responseMap;
        } else {
            responseMap.put("examClrListTotal", examClrListTotal);
        }
        Integer totalCapacity = examClrService.countTotalCapacityByExamNumber(examNumber);
        responseMap.put("totalCapacity", totalCapacity);
        Integer totalCanContain = totalCapacity - examineeService.countExamineeByExamNumber(examNumber);
        responseMap.put("totalCanContain", totalCanContain);

        PageHelper.startPage(curPage, 6);
        List<ExamClr> examClrList = examClrService.getExamClrListByExamNumber(examNumber);
        List<String> clrNumberList = new ArrayList<>();
        for(ExamClr examClr : examClrList) {
            clrNumberList.add(examClr.getClrNumber());
        }

        List<Classroom> clrList = clrService.getClrListByClrNumberList(clrNumberList);
        List<ExamClrInfo> examClrInfoList = new ArrayList<>();
        for (Classroom curClr : clrList) {
            ExamClrInfo examClrInfo = new ExamClrInfo();

            examClrInfo.setAddress(curClr.getAddress());
            examClrInfo.setClrNumber(curClr.getClrNumber());
            examClrInfo.setCapacity(curClr.getCapacity());
            examClrInfo.setCanContain(curClr.getCapacity() - examineeService.countExamineeByExamNumberAndClrNumber(examNumber, curClr.getClrNumber()));

            examClrInfoList.add(examClrInfo);
        }

        responseMap.put("examClrInfoList", examClrInfoList);

        return responseMap;
    }

    @CrossOrigin
    @PostMapping("/api/addInitAccount") // need filter and interceptor
    @ResponseBody
    public Map<String, Object> addInitAccount(@RequestBody Map<String, Object> requestMap) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("status", 200);

        String initAccount = requestMap.get("initAccount").toString();
        String initPassword = requestMap.get("initPassword").toString();

        if(userService.hasUserName(initAccount)){
            responseMap.put("status", 400);
            return responseMap;
        } else {
            User newUser = new User();
            newUser.setUserName(initAccount);
            newUser.setPassword(initPassword);
            newUser.setRole(3);

            userService.insertSingleExamineeAccount(newUser);
        }

        return responseMap;
    }

    @CrossOrigin
    @PostMapping("/api/uploadAvatar") // not need filter and interceptor
    @ResponseBody
    public Map<String, Object> uploadAvatar(@RequestParam("file") MultipartFile photoFile,
                                            @RequestParam("sessionID") String sessionID,
                                            @RequestParam("userName") String userName,
                                            @RequestParam("newExamineeUserName") String newExamineeUserName) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("status", 200);

        // 检验登录状态
        String keyName = "spring:session:sessions:" + sessionID;
        String hashKey = "sessionAttr:" + sessionID;
        if(!redisTemplate.opsForHash().hasKey(keyName, hashKey)){
            responseMap.put("status", 400);
            return responseMap;
        }
        // 已登录在线

        // 暂存到 temp 文件夹中
        String saveFileName = newExamineeUserName + ".jpg";
        String savePath = imagesTempPath;
        try {
            photoFile.transferTo(new File(savePath + saveFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return responseMap;
    }


    @CrossOrigin
    @PostMapping("/api/redisSaveRegExaminee") // need filter and interceptor
    @ResponseBody
    public Map<String, Object> redisSaveRegExaminee(@RequestBody Map<String, Object> requestMap, HttpServletRequest request) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("status", 200);

        String account = requestMap.get("account").toString();
        if(userService.hasUserName(account)) {
            responseMap.put("status", 401); // 已存在考生用户
            return responseMap;
        }

        RegExaminee regExaminee = setRegExaminee(requestMap);
        if(regExaminee == null) {
            responseMap.put("status", 402); // 教室无空位
            return responseMap;
        }

        HttpSession session = request.getSession();
        String regExamineeSessionID = session.getId();
        responseMap.put("regExamineeSessionID", regExamineeSessionID);
        session.setAttribute(regExamineeSessionID, JSON.toJSONString(regExaminee));

        return responseMap;
    }

    private RegExaminee setRegExaminee(Map<String, Object> requestMap) {
        RegExaminee regExaminee = new RegExaminee();
        String randomClrNumber = randomFindClrNumber(requestMap.get("examNumber").toString());
        if(randomClrNumber.equals("")) {
            return null;
        }
        regExaminee.setClrNumber(randomClrNumber);
        regExaminee.setSeatNumber(randomFindSeatInClr(requestMap.get("examNumber").toString(), randomClrNumber));

        regExaminee.setUserName(requestMap.get("account").toString());
        regExaminee.setPassword(requestMap.get("password").toString());
        regExaminee.setRole((Integer) requestMap.get("role"));
        regExaminee.setTrueName(requestMap.get("trueName").toString());
        regExaminee.setGender(requestMap.get("gender").toString());
        regExaminee.setBirthday(String2SQLDate(requestMap.get("birthday").toString()));
        regExaminee.setIdentity(requestMap.get("identity").toString());
        regExaminee.setPhone(requestMap.get("phone").toString());
        regExaminee.setEmail(requestMap.get("email").toString());
        regExaminee.setPhotoURL(requestMap.get("photoURL").toString());
        regExaminee.setExamNumber(requestMap.get("examNumber").toString());
        regExaminee.setExamineeNumber(requestMap.get("examineeNumber").toString());
        regExaminee.setPaymentStatus((Integer) requestMap.get("paymentStatus"));

        return regExaminee;
    }

    private java.sql.Date String2SQLDate(String dateStr) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date d = null;
        try {
            d = simpleDateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new java.sql.Date(d.getTime());
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

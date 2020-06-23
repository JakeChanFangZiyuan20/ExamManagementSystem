package com.cyj.ems.controller;

import com.alibaba.fastjson.JSON;
import com.cyj.ems.domain.Classroom;
import com.cyj.ems.domain.Exam;
import com.cyj.ems.domain.ExamClr;
import com.cyj.ems.domain.Examinee;
import com.cyj.ems.service.*;
import com.cyj.ems.utils.ArrangeClr;
import com.cyj.ems.utils.ArrangeClr2;
import com.cyj.ems.utils.UploadAndDownload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class AddExamController {

    @Value("${localfilepath.formatfile.addexam}")
    private String formatFileAddExamPath;

    @Value("${localfilepath.temp.addexam.addexam}")
    private String tempFileAddExamPath;

    @Value("${localfilepath.temp.addexam.addexamclr}")
    private String tempFileAddExamAddExamClrPath;

    @Value("${localfilepath.temp.addexam.addexaminee}")
    private String tempFileAddExamAddExamineePath;

    @Value("${localfilepath.temp.addexam.addexammg}")
    private String tempFileAddExamAddExamMgPath;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ExamService examService;

    @Autowired
    private ExamMgService examMgService;

    @Autowired
    private ExamClrService examClrService;

    @Autowired
    private ExamineeService examineeService;

    @Autowired
    private ClrService clrService;

    @CrossOrigin
    @GetMapping("/api/downloadFormatFileFromAddExam") // not need filter and interceptor
    public void downloadFormatFileFromAddExam(String downloadFileName, HttpServletResponse response) {
//        System.out.println(downloadFileName);
        String downloadFilePath = formatFileAddExamPath;
        response = UploadAndDownload.download(downloadFilePath, downloadFileName, response);
    }


    @CrossOrigin
    @PostMapping("/api/uploadXLSFileFromAddExam") // no need filter and interceptor
    @ResponseBody
    public Map<String, Object> uploadXLSFile(@RequestParam("file") MultipartFile xlsFile,
                                             @RequestParam("sessionID") String sessionID,
                                             @RequestParam("userName") String userName,
                                             @RequestParam("uploadFileName") String uploadFileName){
        String savePath = "";
        if(uploadFileName.equals("格式文件-考试负责人列表.xls")) {
            savePath = tempFileAddExamAddExamMgPath;
        } else if(uploadFileName.equals("格式文件-考试教室列表.xls")) {
            savePath = tempFileAddExamAddExamClrPath;
        } else if(uploadFileName.equals("格式文件-考生列表.xls")) {
            savePath = tempFileAddExamAddExamineePath;
        }
        String saveName = userName + ".xls";
        return UploadAndDownload.uploadFile(redisTemplate, xlsFile, sessionID, savePath+ saveName);
    }

    @CrossOrigin
    @PostMapping("/api/removeUploadFileFromAddExam") // need filter and interceptor
    @ResponseBody
    public Map<String, Object> removeUploadFile(@RequestBody Map<String, Object> requestMap) {
//        System.out.println(requestMap.get("sessionID"));
        String filePath = tempFileAddExamPath + requestMap.get("uploadFilePath").toString() + "\\";
        String fileName = requestMap.get("userName").toString() + ".xls";
        return UploadAndDownload.removeUploadFile(filePath, fileName);
    }

    private Date String2SQLDate(String dateStr) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date d = null;
        try {
            d = simpleDateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date(d.getTime());
    }

    private Time String2SQLTime(String timeStr) {
        SimpleDateFormat format = new SimpleDateFormat("HH-mm");
        java.util.Date d = null;
        try {
            d = format.parse(timeStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Time(d.getTime());
    }

    @CrossOrigin
    @PostMapping("/api/addExam") // need filter and interceptor
    @ResponseBody
    public Map<String, Object> addExam(@RequestBody Map<String, Object> requestMap) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("uploadStatus", 200);

        Exam newExam = new Exam();
//        System.out.println(JSON.toJSONString(requestMap));
        newExam.setExamNumber(requestMap.get("examNumber").toString());
        newExam.setExamName(requestMap.get("examName").toString());
        newExam.setExamDate(String2SQLDate(requestMap.get("examDate").toString()));
        newExam.setStartTime(String2SQLTime(requestMap.get("examStartTime").toString()));
        newExam.setEndTime(String2SQLTime(requestMap.get("examEndTime").toString()));
        newExam.setRegPrice((Integer) requestMap.get("examRegPrice"));
//        System.out.println(newExam.toString());


        if(examService.hasExam(newExam)) {
            responseMap.replace("uploadStatus", 402); // 402 表示新建考试编号已存在，需保证考试编号唯一性
            return  responseMap;
        }

        String userName = requestMap.get("userName").toString();
        Map<String, Object> processResultMap = processClrFileAndExamineeFile(userName);
        Integer processResultSign = (Integer) processResultMap.get("sign");
        if(processResultSign == 200) {
            List<ExamClr> examClrList = (List<ExamClr>) processResultMap.get("examClrList");
            List<Examinee> examineeList = (List<Examinee>) processResultMap.get("examineeList");

            boolean isRandomArrange = requestMap.get("isRandomArrange").toString().equals("true");
            if(isRandomArrange) {
                String methodStr = requestMap.get("randomArrangeMethod").toString();
                Integer randomMethod = 0;
                if(methodStr.equals("1")) randomMethod = 1;
                else if(methodStr.equals("2")) randomMethod = 2;
                if(randomMethod == 1) {
                    examineeList = randomMethod01(examClrList, examineeList);
                } else {
                    examineeList = randomMethod02(examClrList, examineeList);
                }
            }

            examService.insertExam(newExam);
            examMgService.insertExamMgList(tempFileAddExamAddExamMgPath + userName + ".xls");
            examClrService.insertExamClrList(examClrList);
            examineeService.insertExamineeList(examineeList);
        } else if(processResultSign == 401) {
            responseMap.replace("uploadStatus", 401); // 401 表示教室容量小于考试人数，数据库无改动
            return  responseMap;
        }

        return responseMap;
    }

    private Map<String, Object> processClrFileAndExamineeFile(String userName) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("sign", 200); // 200 表示 ClrFile 和 ExamineeFile 处理成功
        String clrFileName = tempFileAddExamAddExamClrPath + userName + ".xls";
        List<ExamClr> examClrList = examClrService.getExamClrListFromUploadFile(clrFileName);
        // 统计可容纳人数
        Integer clrCapacity = calculateClrCapacity(examClrList);

        String examineeFileName = tempFileAddExamAddExamineePath + userName + ".xls";
        List<Examinee> examineeList = examineeService.getExamineeListFromUploadFile(examineeFileName);
        Integer examineeNumber = examineeList.size();

        if(clrCapacity < examineeNumber){
            resultMap.replace("sign", 401); // 401 表示教室容量小于考试人数，数据库无改动
            return resultMap;
        }

        // 教室容量大于考生数目
        resultMap.put("examClrList", examClrList);
        resultMap.put("examineeList", examineeList);

        return resultMap;
    }

    private Integer calculateClrCapacity(List<ExamClr> examClrList){
        Integer result = 0;

        List<String> clrNumberList = new ArrayList<>();
        for(ExamClr examClr : examClrList) {
            clrNumberList.add(examClr.getClrNumber());
        }

        result = clrService.getClrCapacityByClrNumberList(clrNumberList);

        return result;
    }

    private List<Classroom> getClrListFromExamClrList(List<ExamClr> examClrList) {
        List<String> clrNumberList = new ArrayList<>();
        for(ExamClr examClr : examClrList){
            clrNumberList.add(examClr.getClrNumber());
        }
        return clrService.getClrListByClrNumberList(clrNumberList);
    }

    private List<Integer> getRandomList(Integer range, Integer number){ // 从 [1, range] 中随机选取 number
        HashSet<Integer> seatSet = new HashSet<>();
        while(seatSet.size() != number) {
            seatSet.add((int) (Math.random() * range + 1));
        }
        return new ArrayList<>(seatSet);
    }

    private List<Examinee> randomMethod01(List<ExamClr> examClrList, List<Examinee> examineeList) {
        List<Classroom> clrList = getClrListFromExamClrList(examClrList);

        List<ArrangeClr> arrangeClrList = new ArrayList<>();
        Integer seatCapacity = 0;
        for(int i = 0; i < clrList.size(); i++) {
            ArrangeClr arrangeClr = new ArrangeClr();
            arrangeClr.setClrNumber(clrList.get(i).getClrNumber());
            arrangeClr.setCapacity(clrList.get(i).getCapacity());
            arrangeClr.setExtra(seatCapacity);
            seatCapacity += arrangeClr.getCapacity();
            if(i == 0){
                arrangeClr.setStartPosition(1);
            } else{
                arrangeClr.setStartPosition(arrangeClrList.get(i - 1).getEndPosition() + 1);
            }
            arrangeClr.setEndPosition(arrangeClr.getCapacity() + arrangeClr.getStartPosition() - 1);
            arrangeClrList.add(arrangeClr);
        }

        Integer examineeNumber = examineeList.size();

        List<Integer> seatPosList = getRandomList(seatCapacity, examineeNumber);

        for(int i = 0; i < examineeList.size(); i++){
            Integer curSeat = seatPosList.get(i);
            String clrNumber = "";
            for(int j = 0; j < arrangeClrList.size(); j++) {
                ArrangeClr arrangeClr = arrangeClrList.get(j);
                if(curSeat >= arrangeClr.getStartPosition() && curSeat <= arrangeClr.getEndPosition()) {
                    clrNumber = arrangeClr.getClrNumber();
                    curSeat -= arrangeClr.getExtra();
                    break;
                }
            }
            examineeList.get(i).setClrNumber(clrNumber);
            examineeList.get(i).setSeatNumber(curSeat);
//            System.out.println(examineeList.get(i).toString());
        }

        return examineeList;
    }

    private List<Examinee> randomMethod02(List<ExamClr> examClrList, List<Examinee> examineeList) {
        Integer clrNumber = examClrList.size();
        Integer examineeNumber = examineeList.size();

        List<Classroom> clrList = getClrListFromExamClrList(examClrList);

        Integer perClrCapacity = examineeNumber / clrNumber;
        Integer surplus = examineeNumber % clrNumber;

        Random random = new Random();
        Collections.shuffle(clrList, random);
        Collections.shuffle(examineeList, random);

        List<ArrangeClr2> arrangeClr2List = new ArrayList<>();
        for(Classroom clr : clrList) {
            ArrangeClr2 arrangeClr2 = new ArrangeClr2();
            arrangeClr2.setClrNumber(clr.getClrNumber());
            arrangeClr2.setCapacity(clr.getCapacity());
            arrangeClr2.setExamineeNumber(perClrCapacity);
            arrangeClr2List.add(arrangeClr2);
        }

        if(surplus != 0){
            List<Integer> integerList = getRandomList(clrNumber, surplus);
//            System.out.println(integerList);
            for(int i = 0; i < integerList.size(); i++) {
                Integer curExamineeNumber = arrangeClr2List.get(integerList.get(i) - 1).getExamineeNumber();
                arrangeClr2List.get(integerList.get(i) - 1).setExamineeNumber(curExamineeNumber + 1);
            }
        }

        Integer examineeCount = 0;
        for(ArrangeClr2 arrangeClr2 : arrangeClr2List) {
            List<Integer> curSeatPostList = getRandomList(arrangeClr2.getCapacity(), arrangeClr2.getExamineeNumber());
            for(int i = 0; i < curSeatPostList.size(); i++) {
                examineeList.get(examineeCount).setClrNumber(arrangeClr2.getClrNumber());
                examineeList.get(examineeCount).setSeatNumber(curSeatPostList.get(i));
                examineeCount++;
            }
        }

        return examineeList;
    }

}

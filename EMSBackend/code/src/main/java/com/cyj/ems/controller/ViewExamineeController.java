package com.cyj.ems.controller;

import com.cyj.ems.domain.Exam;
import com.cyj.ems.domain.Examinee;
import com.cyj.ems.domain.User;
import com.cyj.ems.service.ExamService;
import com.cyj.ems.service.ExamineeService;
import com.cyj.ems.service.UserService;
import com.cyj.ems.utils.ExamExaminee;
import com.cyj.ems.utils.UploadAndDownload;
import com.github.pagehelper.PageHelper;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ViewExamineeController {

    @Value("${localfilepath.formatfile.export}")
    private String formatFileExportPath;

    @Value("${localfilepath.temp.export.viewexaminfo.mg}")
    private String tempFileExportViewExamInfoMgPath;

    @Value("${localfilepath.temp.export.viewexaminfo.examinee}")
    private String tempFileExportViewExamInfoExamineePath;

    @Autowired
    UserService userService;

    @Autowired
    ExamService examService;

    @Autowired
    ExamineeService examineeService;

    @CrossOrigin
    @PostMapping("/api/searchExamineeFromViewExaminee") // need filter and interceptor
    @ResponseBody
    public Map<String, Object> searchExaminee(@RequestBody Map<String, Object> requestMap) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("status", 200);

        String userName = requestMap.get("userName").toString();

        User user = userService.getUserByUserName(userName);
        if(user == null) {
            responseMap.put("status", 400);
            return responseMap;
        }
        responseMap.put("userInfo", user);

        return responseMap;
    }

    @CrossOrigin
    @PostMapping("/api/searchExamListFromViewExaminee") // need filter and interceptor
    @ResponseBody
    public Map<String, Object> searchExamListFromViewExaminee(@RequestBody Map<String, Object> requestMap) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("status", 200);

        String userName = requestMap.get("userName").toString();
        Integer curPage = (Integer) requestMap.get("curPage");
        Integer totalSize = examineeService.countExamineeByUserName(userName);
        responseMap.put("examListSize", totalSize);

        PageHelper.startPage(curPage, 6);
        List<Examinee> examineeList = examineeService.getExamineeListByUserName(userName);
        if(examineeList.size() == 0) {
            responseMap.put("status", 400);
            return responseMap;
        }

        List<String> examNumberList = new ArrayList<>();
        for(Examinee examinee : examineeList) {
            examNumberList.add(examinee.getExamNumber());
        }
        List<Exam> examList = examService.getExamListByExamNumberList(examNumberList);

        List<ExamExaminee> examExamineeList = new ArrayList<>();
        for(int i = 0; i < examineeList.size(); i++){
            Exam curExam = examList.get(i);
            Examinee curExaminee = examineeList.get(i);

            ExamExaminee newExamExaminee = new ExamExaminee();
            newExamExaminee.setExamNumber(curExam.getExamNumber());
            newExamExaminee.setExamName(curExam.getExamName());
            newExamExaminee.setExamDate(curExam.getExamDate());
            newExamExaminee.setStartTime(curExam.getStartTime());
            newExamExaminee.setEndTime(curExam.getEndTime());
            newExamExaminee.setRegPrice(curExam.getRegPrice());
            newExamExaminee.setExamineeNumber(curExaminee.getExamineeNumber());
            newExamExaminee.setPaymentStatus(curExaminee.getPaymentStatus());
            newExamExaminee.setClrNumber(curExaminee.getClrNumber());
            newExamExaminee.setSeatNumber(curExaminee.getSeatNumber());

            examExamineeList.add(newExamExaminee);
        }
        responseMap.put("examList", examExamineeList);

        return responseMap;
    }

    @CrossOrigin
    @PostMapping("/api/buildExamInfoList") // need filter and interceptor
    @ResponseBody
    public Map<String, Object> buildExamInfoList(@RequestBody Map<String, Object> requestMap) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("status", 200);

        String userName = requestMap.get("userName").toString();

        List<ExamExaminee> examExamineeList = getExamExamineeList(userName);
        if(examExamineeList.size() == 0) {
            responseMap.put("status", 400);
            return responseMap;
        }
        buildExamExamineeListFile(userName, (Integer) requestMap.get("role"), examExamineeList);

        return responseMap;
    }

    private List<ExamExaminee> getExamExamineeList(String userName) {
        List<Examinee> examineeList = examineeService.getExamineeListByUserName(userName);

        List<String> examNumberList = new ArrayList<>();
        for(Examinee examinee : examineeList) {
            examNumberList.add(examinee.getExamNumber());
        }
        List<Exam> examList = examService.getExamListByExamNumberList(examNumberList);

        List<ExamExaminee> examExamineeList = new ArrayList<>();
        for(int i = 0; i < examineeList.size(); i++){
            Exam curExam = examList.get(i);
            Examinee curExaminee = examineeList.get(i);

            ExamExaminee newExamExaminee = new ExamExaminee();
            newExamExaminee.setExamNumber(curExam.getExamNumber());
            newExamExaminee.setExamName(curExam.getExamName());
            newExamExaminee.setExamDate(curExam.getExamDate());
            newExamExaminee.setStartTime(curExam.getStartTime());
            newExamExaminee.setEndTime(curExam.getEndTime());
            newExamExaminee.setRegPrice(curExam.getRegPrice());
            newExamExaminee.setExamineeNumber(curExaminee.getExamineeNumber());
            newExamExaminee.setPaymentStatus(curExaminee.getPaymentStatus());
            newExamExaminee.setClrNumber(curExaminee.getClrNumber());
            newExamExaminee.setSeatNumber(curExaminee.getSeatNumber());

            examExamineeList.add(newExamExaminee);
        }

        return examExamineeList;
    }

    private void buildExamExamineeListFile(String userName, Integer role, List<ExamExaminee> examExamineeList) {
        String loadFileName = formatFileExportPath + "导出格式文件-考生考试信息列表.xls";
        String saveFileName = "";
        if(role == 1 || role == 2) {
            saveFileName = tempFileExportViewExamInfoMgPath;
        } else {
            saveFileName = tempFileExportViewExamInfoExamineePath;
        }
        saveFileName += userName + ".xls";

        // 构建导出文件
        InputStream fileInputStream = null;
        OutputStream fileOutputStream = null;
        try {
            fileInputStream = new FileInputStream(loadFileName);
            fileOutputStream = new FileOutputStream(saveFileName);

            HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
            HSSFSheet sheet = workbook.getSheet("Sheet1");
            // 第一行考生用户名构建
            Row row01 = sheet.getRow(0);
            Cell userNameCell = row01.createCell(1);
            userNameCell.setCellValue(userName);

            // 写入考生考试信息
            for(int i = 0; i < examExamineeList.size(); i++) {
                Row row = sheet.createRow(i + 2);
                ExamExaminee examExaminee = examExamineeList.get(i);

                Cell examNumberCell = row.createCell(0);
                examNumberCell.setCellValue(examExaminee.getExamNumber());

                Cell examNameCell = row.createCell(1);
                examNameCell.setCellValue(examExaminee.getExamName());

                Cell examDateCell = row.createCell(2);
                examDateCell.setCellValue(examExaminee.getExamDate().toString());

                Cell startTimeCell = row.createCell(3);
                startTimeCell.setCellValue(examExaminee.getStartTime().toString());

                Cell endTimeCell = row.createCell(4);
                endTimeCell.setCellValue(examExaminee.getEndTime().toString());

                Cell regPriceCell = row.createCell(5);
                regPriceCell.setCellValue(examExaminee.getRegPrice().toString());

                Cell examineeNumberCell = row.createCell(6);
                examineeNumberCell.setCellValue(examExaminee.getExamineeNumber());

                Cell paymentStatusCell = row.createCell(7);
                paymentStatusCell.setCellValue(examExaminee.getPaymentStatus().toString());

                Cell clrNumberCell = row.createCell(8);
                clrNumberCell.setCellValue(examExaminee.getClrNumber());

                Cell seatNumberCell = row.createCell(9);
                seatNumberCell.setCellValue(examExaminee.getSeatNumber().toString());
            }

            workbook.write(fileOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(fileInputStream != null){
                    fileInputStream.close();
                }
                if(fileOutputStream != null){
                    fileOutputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @CrossOrigin
    @GetMapping("/api/downloadExamInfoList") // not need filter and Interceptor
    public void downloadExamInfoList(String userName, Integer role, HttpServletResponse response) {
        String exportFilePath = "";
        if(role == 1 || role == 2) {
            exportFilePath = tempFileExportViewExamInfoMgPath;
        } else {
            exportFilePath = tempFileExportViewExamInfoExamineePath;
        }
        response = UploadAndDownload.download(exportFilePath, userName + ".xls", response);
    }

}

package com.cyj.ems.controller;

import com.alibaba.fastjson.JSON;
import com.cyj.ems.domain.*;
import com.cyj.ems.service.*;
import com.cyj.ems.utils.ExamineeUserInfo;
import com.cyj.ems.utils.UploadAndDownload;
import com.github.pagehelper.PageHelper;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
public class ViewExamController {

    @Value("${localfilepath.formatfile.export}")
    private String formatFileExportPath;

    @Value("${localfilepath.temp.export.viewexam.exammg}")
    private String tempFileExportViewExamExamMgPath;

    @Value("${localfilepath.temp.export.viewexam.examclr}")
    private String tempFileExportViewExamExamClrPath;

    @Value("${localfilepath.temp.export.viewexam.examexaminee}")
    private String tempFileExportViewExamExamineePath;

    @Autowired
    ExamService examService;

    @Autowired
    ExamMgService examMgService;

    @Autowired
    ExamClrService examClrService;

    @Autowired
    ClrService clrService;

    @Autowired
    ExamineeService examineeService;

    @Autowired
    UserService userService;

    @CrossOrigin
    @PostMapping("/api/searchExam") // need filter and interceptor
    @ResponseBody
    public Map<String, Object> searchExam(@RequestBody Map<String, Object> requestMap) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("searchStatus", 200);

        List<Exam> examList = new ArrayList<>();
        responseMap.put("resultSet", examList);
        responseMap.put("resultSetSize", 0);

        String examNumber = requestMap.get("examNumber").toString();
        String examName = requestMap.get("examName").toString();
        Integer curPage = (Integer) requestMap.get("curPage");

        Integer resultSetSize = 0;
        if (examNumber.length() != 0) {
            Exam exam = examService.getExamByExamNumber(examNumber);
            if (exam == null) {
                responseMap.put("searchStatus", 400); // 400 表示没有查询到
            } else {
                examList.add(exam);
                resultSetSize = 1;
            }
        } else if (examName.length() != 0) {
            PageHelper.startPage(curPage, 6);
            examList = examService.getExamByExamName(examName);
            if (examList.size() == 0) {
                responseMap.put("searchStatus", 400); // 400 表示没有查询到
            }
            resultSetSize = examService.countExamSearchByExamName(examName);
        } else {
            PageHelper.startPage(curPage, 6);
            examList = examService.getAllExam();
            if (examList.size() == 0) {
                responseMap.put("searchStatus", 400); // 400 表示没有查询到
            }
            resultSetSize = examService.countExam();
        }
        responseMap.put("resultSet", examList);
        responseMap.put("resultSetSize", resultSetSize);

        return responseMap;
    }

    @CrossOrigin
    @PostMapping("/api/searchExamMg") // need filter and interceptor
    @ResponseBody
    public Map<String, Object> searchExamMg(@RequestBody Map<String, Object> requestMap) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("searchExamMgStatus", 200);

        String examNumber = requestMap.get("examNumber").toString();
        Integer curPage = (Integer) requestMap.get("curPage");

        PageHelper.startPage(curPage, 6);
        List<ExamMg> examMgList = examMgService.getExamMgListByExamNumber(examNumber);
        if (examMgList.size() == 0) {
            responseMap.put("searchExamMgStatus", 400);
            return responseMap;
        }

        responseMap.put("examMgList", examMgList);
        responseMap.put("examMgListSize", examMgService.countExamMgByExamNumber(examNumber));

        return responseMap;
    }

    @CrossOrigin
    @PostMapping("/api/searchExamClr") // need filter and interceptor
    @ResponseBody
    public Map<String, Object> searchExamClr(@RequestBody Map<String, Object> requestMap) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("searchExamClrStatus", 200);

        String examNumber = requestMap.get("examNumber").toString();
        Integer curPage = (Integer) requestMap.get("curPage");

        PageHelper.startPage(curPage, 6);
        List<ExamClr> examClrList = examClrService.getExamClrListByExamNumber(examNumber);
        if (examClrList.size() == 0) {
            responseMap.put("searchExamMgStatus", 400);
            return responseMap;
        }

        List<String> clrNumberList = new ArrayList<>();
        for (ExamClr examClr : examClrList) {
            clrNumberList.add(examClr.getClrNumber());
        }
        List<Classroom> classroomList = clrService.getClrListByClrNumberList(clrNumberList);

        responseMap.put("examClrList", classroomList);
        responseMap.put("examClrListSize", examClrService.countExamClrByExamNumber(examNumber));

        return responseMap;
    }

    @CrossOrigin
    @PostMapping("/api/searchExaminee") // need filter and interceptor
    @ResponseBody
    public Map<String, Object> searchExaminee(@RequestBody Map<String, Object> requestMap) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("searchExamineeStatus", 200);

        String examNumber = requestMap.get("examNumber").toString();
        Integer curPage = (Integer) requestMap.get("curPage");

        PageHelper.startPage(curPage, 6);
        List<Examinee> examineeList = examineeService.getExamineeListByExamNumber(examNumber);
        if (examineeList.size() == 0) {
            responseMap.put("searchExamMgStatus", 400);
            return responseMap;
        }
        responseMap.put("examineeListSize", examineeService.countExamineeByExamNumber(examNumber));

        List<String> userNameList = new ArrayList<>();
        for (Examinee examinee : examineeList) {
            userNameList.add(examinee.getUserName());
        }
        List<User> userList = userService.getUserListByUserNameList(userNameList);

        List<ExamineeUserInfo> examineeUserInfoList = new ArrayList<>();
        for (int i = 0; i < userList.size(); i++) {
            ExamineeUserInfo examineeUserInfo = new ExamineeUserInfo();
            Examinee examinee = examineeList.get(i);
            User user = userList.get(i);
            examineeUserInfo.setExamineeNumber(examinee.getExamineeNumber());
            examineeUserInfo.setUserName(user.getUserName());
            examineeUserInfo.setTrueName(user.getTrueName());
            examineeUserInfo.setIdentity(user.getIdentity());
            examineeUserInfo.setPaymentStatus(examinee.getPaymentStatus());
            examineeUserInfo.setClrNumber(examinee.getClrNumber());
            examineeUserInfo.setSeatNumber(examinee.getSeatNumber());
            examineeUserInfo.setGender(user.getGender());
            examineeUserInfo.setBirthday(user.getBirthday());
            examineeUserInfo.setPhone(user.getPhone());

            examineeUserInfoList.add(examineeUserInfo);
        }
        responseMap.put("examineeList", examineeUserInfoList);

        return responseMap;
    }

    @CrossOrigin
    @PostMapping("/api/exportExamMgList") // need filter and interceptor
    @ResponseBody
    public Map<String, Object> exportExamMgList(@RequestBody Map<String, Object> requestMap) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("status", 200);

        String examNumber = requestMap.get("examNumber").toString();
        Exam exam = examService.getExamByExamNumber(examNumber);
        List<ExamMg> examMgList = examMgService.getExamMgListByExamNumber(examNumber);
        if (examMgList.size() == 0) {
            responseMap.put("searchExamMgStatus", 400);
            return responseMap;
        }
        buildExamMgList(exam.getExamNumber(), exam.getExamName(), examMgList);

        return responseMap;
    }

    private void buildExamMgList(String examNumber, String examName, List<ExamMg> examMgList) {
        String loadFileName = formatFileExportPath + "导出格式文件-考试负责人列表.xls";
        String saveFileName = tempFileExportViewExamExamMgPath + examNumber + "考试负责人列表.xls";

        InputStream fileInputStream = null;
        OutputStream fileOutputStream = null;
        try {
            fileInputStream = new FileInputStream(loadFileName);
            fileOutputStream = new FileOutputStream(saveFileName);

            HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
            HSSFSheet sheet = workbook.getSheet("Sheet1");

            Row row01 = sheet.getRow(0);
            Cell examNumberCell = row01.createCell(1);
            examNumberCell.setCellValue(examNumber);

            Row row02 = sheet.getRow(1);
            Cell examNameCell = row02.createCell(1);
            examNameCell.setCellValue(examName);

            for (int i = 0; i < examMgList.size(); i++) {
                Row row = sheet.createRow(i + 3);
                ExamMg examMg = examMgList.get(i);

                Cell clrMgNameCell = row.createCell(0);
                clrMgNameCell.setCellValue(examMg.getMgName());

                Cell clrMgNamePhoneCell = row.createCell(1);
                clrMgNamePhoneCell.setCellValue(examMg.getMgPhone());
            }

            workbook.write(fileOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @CrossOrigin
    @GetMapping("/api/downloadExamMgList") // not need filter and interceptor
    public void downloadExamMgList(String examNumber, HttpServletResponse response) {
        String exportFilePath = tempFileExportViewExamExamMgPath;
        response = UploadAndDownload.download(exportFilePath, examNumber + "考试负责人列表.xls", response);
    }

    @CrossOrigin
    @PostMapping("/api/exportExamClrList") // need filter and interceptor
    @ResponseBody
    public Map<String, Object> exportExamClrList(@RequestBody Map<String, Object> requestMap) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("status", 200);

        String examNumber = requestMap.get("examNumber").toString();
        Exam exam = examService.getExamByExamNumber(examNumber);
        List<ExamClr> examClrList = examClrService.getExamClrListByExamNumber(examNumber);
        if (examClrList.size() == 0) {
            responseMap.put("searchExamMgStatus", 400);
            return responseMap;
        }

        List<String> clrNumberList = new ArrayList<>();
        for (ExamClr examClr : examClrList) {
            clrNumberList.add(examClr.getClrNumber());
        }
        List<Classroom> clrList = clrService.getClrListByClrNumberList(clrNumberList);

        buildExamClrList(exam.getExamNumber(), exam.getExamName(), clrList);

        return responseMap;
    }

    private void buildExamClrList(String examNumber, String examName, List<Classroom> clrList) {
        String loadFileName = formatFileExportPath + "导出格式文件-考试教室列表.xls";
        String saveFileName = tempFileExportViewExamExamClrPath + examNumber + "考试教室列表.xls";

        InputStream fileInputStream = null;
        OutputStream fileOutputStream = null;
        try {
            fileInputStream = new FileInputStream(loadFileName);
            fileOutputStream = new FileOutputStream(saveFileName);

            HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
            HSSFSheet sheet = workbook.getSheet("Sheet1");

            Row row01 = sheet.getRow(0);
            Cell examNumberCell = row01.createCell(1);
            examNumberCell.setCellValue(examNumber);

            Row row02 = sheet.getRow(1);
            Cell examNameCell = row02.createCell(1);
            examNameCell.setCellValue(examName);

            for (int i = 0; i < clrList.size(); i++) {
                Row row = sheet.createRow(i + 3);
                Classroom clr = clrList.get(i);

                Cell addressCell = row.createCell(0);
                addressCell.setCellValue(clr.getAddress());

                Cell clrNumberCell = row.createCell(1);
                clrNumberCell.setCellValue(clr.getClrNumber());

                Cell capacityCell = row.createCell(2);
                capacityCell.setCellValue(clr.getCapacity());
            }

            workbook.write(fileOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @CrossOrigin
    @GetMapping("/api/downloadExamClrList") // not need filter and interceptor
    public void downloadExamClrList(String examNumber, HttpServletResponse response) {
        String exportFilePath = tempFileExportViewExamExamClrPath;
        response = UploadAndDownload.download(exportFilePath, examNumber + "考试教室列表.xls", response);
    }

    @CrossOrigin
    @PostMapping("/api/exportExamineeList") // need filter and interceptor
    @ResponseBody
    public Map<String, Object> exportExamineeList(@RequestBody Map<String, Object> requestMap) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("status", 200);

        String examNumber = requestMap.get("examNumber").toString();
        Exam exam = examService.getExamByExamNumber(examNumber);
        List<Examinee> examineeList = examineeService.getExamineeListByExamNumber(examNumber);
        if (examineeList.size() == 0) {
            responseMap.put("status", 400);
            return responseMap;
        }

        List<String> userNameList = new ArrayList<>();
        for (Examinee examinee : examineeList) {
            userNameList.add(examinee.getUserName());
        }
        List<User> userList = userService.getUserListByUserNameList(userNameList);

        List<ExamineeUserInfo> examineeUserInfoList = new ArrayList<>();
        for (int i = 0; i < userList.size(); i++) {
            ExamineeUserInfo examineeUserInfo = new ExamineeUserInfo();
            Examinee examinee = examineeList.get(i);
            User user = userList.get(i);
            examineeUserInfo.setExamineeNumber(examinee.getExamineeNumber());
            examineeUserInfo.setUserName(user.getUserName());
            examineeUserInfo.setTrueName(user.getTrueName());
            examineeUserInfo.setIdentity(user.getIdentity());
            examineeUserInfo.setPaymentStatus(examinee.getPaymentStatus());
            examineeUserInfo.setClrNumber(examinee.getClrNumber());
            examineeUserInfo.setSeatNumber(examinee.getSeatNumber());
            examineeUserInfo.setGender(user.getGender());
            examineeUserInfo.setBirthday(user.getBirthday());
            examineeUserInfo.setPhone(user.getPhone());

            examineeUserInfoList.add(examineeUserInfo);
        }

        buildExamineeList(exam.getExamNumber(), exam.getExamName(), examineeUserInfoList);

        return responseMap;
    }

    private void buildExamineeList(String examNumber, String examName, List<ExamineeUserInfo> examineeUserInfoList) {
        String loadFileName = formatFileExportPath + "导出格式文件-考生列表.xls";
        String saveFileName = tempFileExportViewExamExamineePath + examNumber + "考生列表.xls";

        InputStream fileInputStream = null;
        OutputStream fileOutputStream = null;
        try {
            fileInputStream = new FileInputStream(loadFileName);
            fileOutputStream = new FileOutputStream(saveFileName);

            HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
            HSSFSheet sheet = workbook.getSheet("Sheet1");

            Row row01 = sheet.getRow(0);
            Cell examNumberCell = row01.createCell(1);
            examNumberCell.setCellValue(examNumber);

            Row row02 = sheet.getRow(1);
            Cell examNameCell = row02.createCell(1);
            examNameCell.setCellValue(examName);

            for (int i = 0; i < examineeUserInfoList.size(); i++) {
                Row row = sheet.createRow(i + 3);
                ExamineeUserInfo examineeUserInfo = examineeUserInfoList.get(i);

                Cell examineeNumberCell = row.createCell(0);
                examineeNumberCell.setCellValue(examineeUserInfo.getExamineeNumber() != null ? examineeUserInfo.getExamineeNumber() : "");

                Cell userNameCell = row.createCell(1);
                userNameCell.setCellValue(examineeUserInfo.getUserName() != null ? examineeUserInfo.getUserName() : "");

                Cell trueNameCell = row.createCell(2);
                trueNameCell.setCellValue(examineeUserInfo.getTrueName() != null ? examineeUserInfo.getTrueName() : "");

                Cell identityCell = row.createCell(3);
                identityCell.setCellValue(examineeUserInfo.getIdentity() != null ? examineeUserInfo.getIdentity() : "");

                Cell paymentStatusCell = row.createCell(4);
                if (examineeUserInfo.getPaymentStatus() != null) {
                    paymentStatusCell.setCellValue(examineeUserInfo.getPaymentStatus());
                }

                Cell clrNumberCell = row.createCell(5);
                clrNumberCell.setCellValue(examineeUserInfo.getClrNumber() != null ? examineeUserInfo.getClrNumber() : "");

                Cell seatNumberCell = row.createCell(6);
                if (examineeUserInfo.getSeatNumber() != null) {
                    seatNumberCell.setCellValue(examineeUserInfo.getSeatNumber());
                }

                Cell genderCell = row.createCell(7);
                genderCell.setCellValue(examineeUserInfo.getGender() != null ? examineeUserInfo.getGender() : "");

                Cell birthdayCell = row.createCell(8);
                birthdayCell.setCellValue(examineeUserInfo.getBirthday() != null ? examineeUserInfo.getBirthday().toString() : "");

                Cell phoneCell = row.createCell(9);
                phoneCell.setCellValue(examineeUserInfo.getPhone() != null ? examineeUserInfo.getPhone() : "");
            }

            workbook.write(fileOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @CrossOrigin
    @GetMapping("/api/downloadExamineeList") // not need filter and interceptor
    public void downloadExamineeList(String examNumber, HttpServletResponse response) {
        String exportFilePath = tempFileExportViewExamExamineePath;
        response = UploadAndDownload.download(exportFilePath, examNumber + "考生列表.xls", response);
    }

    @CrossOrigin
    @PostMapping("/api/viewExamClrCheck") // need filter and interceptor
    @ResponseBody
    public Map<String, Object> viewExamClrCheck(@RequestBody Map<String, Object> requestMap) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("status", 200);

        String examNumber = requestMap.get("examNumber").toString();
        Exam exam = examService.getExamByExamNumber(examNumber);
        List<ExamClr> examClrList = examClrService.getExamClrListByExamNumber(examNumber);
        if(exam == null) {
            responseMap.put("status", 400);
            return responseMap;
        }
        List<String> clrNumber = new ArrayList<>();
        if(examClrList.size() != 0) {
            for(ExamClr examClr : examClrList){
                clrNumber.add(examClr.getClrNumber());
            }
        }
        List<Classroom> clrList = clrService.getClrListByClrNumberList(clrNumber);

        responseMap.put("examInfo", exam);
        responseMap.put("examClrList", clrList);

        return responseMap;
    }

    @CrossOrigin
    @PostMapping("/api/viewExamineeCheck") // need filter and interceptor
    @ResponseBody
    public Map<String, Object> viewExamineeCheck(@RequestBody Map<String, Object> requestMap){
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("status", 200);

        String examNumber = requestMap.get("examNumber").toString();
        Exam exam = examService.getExamByExamNumber(examNumber);
        List<Examinee> examineeList = examineeService.getExamineeListByExamNumber(examNumber);
        if (examineeList.size() == 0) {
            responseMap.put("searchExamMgStatus", 400);
            return responseMap;
        }
        List<String> userNameList = new ArrayList<>();
        for (Examinee examinee : examineeList) {
            userNameList.add(examinee.getUserName());
        }
        List<User> userList = userService.getUserListByUserNameList(userNameList);

        List<ExamineeUserInfo> examineeUserInfoList = new ArrayList<>();
        for (int i = 0; i < userList.size(); i++) {
            ExamineeUserInfo examineeUserInfo = new ExamineeUserInfo();
            Examinee examinee = examineeList.get(i);
            User user = userList.get(i);
            examineeUserInfo.setExamineeNumber(examinee.getExamineeNumber());
            examineeUserInfo.setIdentity(user.getIdentity());
            examineeUserInfo.setUserName(user.getUserName());
            examineeUserInfo.setTrueName(user.getTrueName());
            examineeUserInfo.setClrNumber(examinee.getClrNumber());
            examineeUserInfo.setSeatNumber(examinee.getSeatNumber());

            examineeUserInfoList.add(examineeUserInfo);
        }

        responseMap.put("examInfo", exam);
        responseMap.put("examineeCheckList", examineeUserInfoList);

        return responseMap;
    }
}

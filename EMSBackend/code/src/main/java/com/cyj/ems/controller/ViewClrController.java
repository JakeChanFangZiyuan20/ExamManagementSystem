package com.cyj.ems.controller;

import com.cyj.ems.domain.Classroom;
import com.cyj.ems.domain.ClrMg;
import com.cyj.ems.service.ClrMgService;
import com.cyj.ems.service.ClrService;
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
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ViewClrController {

    @Value("${localfilepath.formatfile.export}")
    private String formatFileExportPath;

    @Value("${localfilepath.temp.export.viewclr}")
    private String tempExportViewClrPath;

    @Autowired
    ClrService clrService;

    @Autowired
    ClrMgService clrMgService;

    @CrossOrigin
    @PostMapping("/api/searchClr") // need filter and interceptor
    @ResponseBody
    public Map<String, Object> searchClr(@RequestBody Map<String, Object> requestMap) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("searchClrStatus", 200);

        String clrNumber = requestMap.get("clrNumber").toString();
        Integer curPage = (Integer) requestMap.get("curPage");

        List<Classroom> clrList = new ArrayList<>();
        Integer clrListSize = 0;
        if (clrNumber.length() != 0) {
            Classroom classroom = clrService.getClassroomByClrNumber(clrNumber);
            if (classroom != null) {
                clrList.add(classroom);
                clrListSize = 1;
            } else {
                responseMap.put("searchClrStatus", 400);
                return responseMap;
            }
        } else {
            PageHelper.startPage(curPage, 6);
            clrList = clrService.getAllClr();
            if (clrList.size() == 0) {
                responseMap.put("searchClrStatus", 400);
                return responseMap;
            }
            clrListSize = clrService.countClr();
        }
        responseMap.put("clrList", clrList);
        responseMap.put("clrListSize", clrListSize);

        return responseMap;
    }

    @CrossOrigin
    @PostMapping("/api/searchClrMg") // need filter and interceptor
    @ResponseBody
    public Map<String, Object> searchClrMg(@RequestBody Map<String, Object> requestMap) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("searchClrMgStatus", 200);

        String clrNumber = requestMap.get("clrNumber").toString();
        Integer curPage = (Integer) requestMap.get("curPage");

        Integer clrID = clrService.getClrIDByClrNumber(clrNumber);
        PageHelper.startPage(curPage, 6);
        List<ClrMg> clrMgList = clrMgService.getClrMgByClrID(clrID);
        Integer clrMgListSize = clrMgService.countClrMgByClrID(clrID);
        if (clrMgList.size() == 0) {
            responseMap.put("searchClrMgStatus", 400);
            return responseMap;
        }
        responseMap.put("clrMgList", clrMgList);
        responseMap.put("clrMgListSize", clrMgListSize);

        return responseMap;
    }

    @CrossOrigin
    @PostMapping("/api/deleteClrMg") // need filter and interceptor
    @ResponseBody
    public Map<String, Object> deleteClrMg(@RequestBody Map<String, Object> requestMap) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("deleteClrMgStatus", 200);

        String clrNumber = requestMap.get("clrNumber").toString();
        String mgName = requestMap.get("mgName").toString();
        String mgPhone = requestMap.get("mgPhone").toString();

        Integer clrID = clrService.getClrIDByClrNumber(clrNumber);
        if (clrMgService.hasClrMg(clrID, mgName, mgPhone)) {
            clrMgService.deleteClrMg(clrID, mgName, mgPhone);
        }

        return responseMap;
    }

    @CrossOrigin
    @PostMapping("/api/updateClrMg") // need filter and interceptor
    @ResponseBody
    public Map<String, Object> updateClrMg(@RequestBody Map<String, Object> requestMap) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("updateClrMgStatus", 200);

        String clrNumber = requestMap.get("clrNumber").toString();
        String orgMgName = requestMap.get("orgMgName").toString();
        String newMgName = requestMap.get("newMgName").toString();
        String orgMgPhone = requestMap.get("orgMgPhone").toString();
        String newMgPhone = requestMap.get("newMgPhone").toString();

        Integer clrID = clrService.getClrIDByClrNumber(clrNumber);
        clrMgService.updateClrMg(clrID, orgMgName, newMgName, orgMgPhone, newMgPhone);

        return responseMap;
    }

    @CrossOrigin
    @PostMapping("/api/exportClrMgList") // need filter and interceptor
    @ResponseBody
    public Map<String, Object> exportClrMgList(@RequestBody Map<String, Object> requestMap){
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("status", 200);

        String clrNumber = requestMap.get("clrNumber").toString();
        Classroom classroom = clrService.getClassroomByClrNumber(clrNumber);
        Integer clrID = classroom.getClrID();
        String address = classroom.getAddress();
        List<ClrMg> clrMgList = clrMgService.getClrMgByClrID(clrID);
        if(clrMgList.size() == 0) {
            responseMap.put("status", 400);
            return responseMap;
        }
        buildClrMgList(address ,clrNumber, clrMgList);

        return responseMap;
    }

    private void buildClrMgList(String address, String clrNumber, List<ClrMg> clrMgList) {
        String loadFileName = formatFileExportPath + "导出格式文件-教室负责人列表.xls";
        String saveFileName = tempExportViewClrPath + clrNumber + "教室负责人列表.xls";

        InputStream fileInputStream = null;
        OutputStream fileOutputStream = null;
        try {
            fileInputStream = new FileInputStream(loadFileName);
            fileOutputStream = new FileOutputStream(saveFileName);

            HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
            HSSFSheet sheet = workbook.getSheet("Sheet1");

            Row row01 = sheet.getRow(0);
            Cell addressCell = row01.createCell(1);
            addressCell.setCellValue(address);

            Row row02 = sheet.getRow(1);
            Cell clrNumberCell = row02.createCell(1);
            clrNumberCell.setCellValue(clrNumber);

            for(int i = 0; i < clrMgList.size(); i++){
                Row row = sheet.createRow(i + 3);
                ClrMg clrMg = clrMgList.get(i);

                Cell clrMgNameCell = row.createCell(0);
                clrMgNameCell.setCellValue(clrMg.getMgName());

                Cell clrMgNamePhoneCell = row.createCell(1);
                clrMgNamePhoneCell.setCellValue(clrMg.getMgPhone());
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
    @GetMapping("/api/downloadClrMgInfoList") // not need filter and interceptor
    public void downloadClrMgList(String clrNumber, HttpServletResponse response) {
        String exportFilePath = tempExportViewClrPath;
        response = UploadAndDownload.download(exportFilePath, clrNumber + "教室负责人列表.xls", response);
    }

}

package com.cyj.ems.service;

import com.cyj.ems.dao.ExamClrDao;
import com.cyj.ems.domain.ExamClr;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service("examClrService")
public class ExamClrService {

    @Autowired
    ExamClrDao examClrDao;

    @Autowired
    ClrService clrService;

    public List<ExamClr> getExamClrListByClrNumber(String clrNumber) {
        return examClrDao.getExamClrListByClrID(clrNumber);
    }

    public List<ExamClr> getExamClrListByExamNumber(String examNumber) {
        return examClrDao.getExamClrListByExamNumber(examNumber);
    }

    public Integer countExamClrByExamNumber(String examNumber) {
        return examClrDao.countExamClrByExamNumber(examNumber);
    }

    public List<ExamClr> getExamClrListFromUploadFile(String fileName) {
        InputStream fileInputStream = null;
        List<ExamClr> examClrList = new ArrayList<>();

        try {
            fileInputStream = new FileInputStream(fileName);
            HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream); // HSSFWorkBook for xls
            HSSFSheet sheet = workbook.getSheet("Sheet1");
            int rowNumber = sheet.getPhysicalNumberOfRows();
//            System.out.println(rowNumber);
            for (int i = 1; i < rowNumber; i++) {
                Row row = sheet.getRow(i);

                ExamClr examClr = new ExamClr();

                // col 1 examNumber
                Cell cell01 = row.getCell(0);
                examClr.setExamNumber(cell01.getStringCellValue());
                if(examClr.getExamNumber().length() == 0) break;

                // col 2 clrNumber
                Cell cell02 = row.getCell(1);
                examClr.setClrNumber(cell02.getStringCellValue());

                examClrList.add(examClr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(fileInputStream != null){
                    fileInputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return examClrList;
    }

    public void insertExamClrList(List<ExamClr> examClrList) {
        examClrDao.insertExamClrList(examClrList);
    }

    public Integer countTotalCapacityByExamNumber(String examNumber) {
        List<ExamClr> examClrList = examClrDao.getExamClrListByExamNumber(examNumber);
        List<String> clrNumberList = new ArrayList<>();
        for(ExamClr examClr : examClrList) {
            clrNumberList.add(examClr.getClrNumber());
        }
        return clrService.getClrCapacityByClrNumberList(clrNumberList);
    }
}

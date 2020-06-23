package com.cyj.ems.service;

import com.cyj.ems.dao.ExamMgDao;
import com.cyj.ems.domain.ExamMg;
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

@Service("examMgService")
public class ExamMgService {

    @Autowired
    ExamMgDao examMgDao;

    public List<ExamMg> getExamMgListByExamNumber(String examNumber) {
        return examMgDao.getExamMgListByExamNumber(examNumber);
    }

    public Integer insertExamMgList(String fileName) {
        Integer sign = 200; // 200 表示 MgFile 处理成功

        InputStream fileInputStream = null;
        List<ExamMg> examMgList = new ArrayList<>();

        try {
            fileInputStream = new FileInputStream(fileName);
            HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream); // HSSFWorkBook for xls
            HSSFSheet sheet = workbook.getSheet("Sheet1");
            int rowNumber = sheet.getPhysicalNumberOfRows();
//            System.out.println(rowNumber);
            for (int i = 1; i < rowNumber; i++) {
                Row row = sheet.getRow(i);

                ExamMg examMg = new ExamMg();

                // col 1 ExamNumber
                Cell cell01 = row.getCell(0);
                examMg.setExamNumber(cell01.getStringCellValue());
                if(examMg.getExamNumber().length() == 0) break;

                // col 2 MgName
                Cell cell02 = row.getCell(1);
                examMg.setMgName(cell02.getStringCellValue());

                // col 3 MgPhone
                Cell cell03 = row.getCell(2);
                examMg.setMgPhone(cell03.getStringCellValue());

                examMgList.add(examMg);
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

        if(examMgList.size() == 0){
            return 400; // 表示上传考试负责人列表无内容
        }
        examMgDao.insertExamMgList(examMgList);
        return sign;
    }

    public Integer countExamMgByExamNumber(String examNumber) {
        return examMgDao.countExamMgByExamNumber(examNumber);
    }
}

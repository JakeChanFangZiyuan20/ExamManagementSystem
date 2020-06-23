package com.cyj.ems.service;

import com.cyj.ems.dao.ClrMgDao;
import com.cyj.ems.domain.ClrMg;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service("clrMgService")
public class ClrMgService {

    @Autowired
    ClrMgDao clrMgDao;

    public List<ClrMg> getClrMgByClrID(Integer clrID) {
        return clrMgDao.getClrMgByClrID(clrID);
    }

    public Integer countClrMgByClrID(Integer clrID) {
        return clrMgDao.countClrMgByClrID(clrID);
    }

    public boolean hasClrMg(Integer clrID, String mgName, String mgPhone) {
        return clrMgDao.hasClrMg(clrID, mgName, mgPhone) != null;
    }

    public void deleteClrMg(Integer clrID, String mgName, String mgPhone) {
        clrMgDao.deleteClrMg(clrID, mgName, mgPhone);
    }

    public void insertClrMgList(Integer clrID, String fileName) {
        InputStream fileInputStream = null;
        List<ClrMg> clrMgList = new ArrayList<>();

        try {
            fileInputStream = new FileInputStream(fileName);
            HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream); // HSSFWorkBook for xls
            HSSFSheet sheet = workbook.getSheet("Sheet1");
            int rowNumber = sheet.getPhysicalNumberOfRows();
//            System.out.println(rowNumber);
            for(int i = 1; i < rowNumber; i++) {
                Row row = sheet.getRow(i);

                ClrMg clrMg = new ClrMg();
                clrMg.setClrID(clrID);

                // col 1
                Cell cell01 = row.getCell(0);
                clrMg.setMgName(cell01.getStringCellValue());
                if(clrMg.getMgName().length() == 0) break;

                // col 2
                Cell cell02 = row.getCell(1);
                clrMg.setMgPhone(cell02.getStringCellValue());

                clrMgList.add(clrMg);
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

        clrMgDao.insertClrMgList(clrMgList);
    }

    public void updateClrMg(Integer clrID, String orgMgName, String newMgName, String orgMgPhone, String newMgPhone) {
        clrMgDao.updateClrMg(clrID, orgMgName, newMgName, orgMgPhone, newMgPhone);
    }

}

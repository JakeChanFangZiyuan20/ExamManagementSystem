package com.cyj.ems.service;

import com.cyj.ems.dao.ExamineeDao;
import com.cyj.ems.domain.Examinee;
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

@Service("examineeService")
public class ExamineeService {

    @Autowired
    ExamineeDao examineeDao;

    public Examinee getExamineeByExamineeID(Integer examineeID) {
        return examineeDao.getExamineeByExamineeID(examineeID);
    }

    public List<Examinee> getExamineeListByExamNumber(String examNumber) {
        return examineeDao.getExamineeListByExamNumber(examNumber);
    }

    public List<Examinee> getExamineeListByUserName(String userName) {
        return examineeDao.getExamineeListByUserName(userName);
    }

    public Integer countExamineeByExamNumber(String examNumber){
        return examineeDao.countExamineeByExamNumber(examNumber);
    }

    public List<Examinee> getExamineeByExamineeNumber(String examineeNumber) {
        return examineeDao.getExamineeByExamineeNumber(examineeNumber);
    }

    public Integer countExamineeByUserName(String userName) {
        return examineeDao.countExamineeByUserName(userName);
    }

    public List<Examinee> getExamineeListFromUploadFile(String fileName) {
        InputStream fileInputStream = null;
        List<Examinee> examineeList = new ArrayList<>();

        try {
            fileInputStream = new FileInputStream(fileName);
            HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream); // HSSFWorkBook for xls
            HSSFSheet sheet = workbook.getSheet("Sheet1");
            int rowNumber = sheet.getPhysicalNumberOfRows();
//            System.out.println(rowNumber);
            for (int i = 1; i < rowNumber; i++) {
                Row row = sheet.getRow(i);

                Examinee examinee = new Examinee();

                // col 1 examNumber
                Cell cell01 = row.getCell(0);
                examinee.setExamNumber(cell01.getStringCellValue());
                if(examinee.getExamNumber().length() == 0) break;

                // col 2 UserName
                Cell cell02 = row.getCell(1);
                examinee.setUserName(cell02.getStringCellValue());

                // col 3 ExamineeNumber
                Cell cell03 = row.getCell(2);
                examinee.setExamineeNumber(cell03.getStringCellValue());

                // col 4 PaymentStatus
                Cell cell04 = row.getCell(3);
                examinee.setPaymentStatus((int) cell04.getNumericCellValue());

                // col 4 PaymentStatus
                Cell cell05 = row.getCell(4);
                examinee.setClrNumber(cell05 == null ? "" : cell05.getStringCellValue());

                // col 5 SeatNumber
                Cell cell06 = row.getCell(5);
                examinee.setSeatNumber(cell06 == null ? 0 : (int) cell05.getNumericCellValue());

                examineeList.add(examinee);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return examineeList;
    }

    public void insertExamineeList(List<Examinee> examineeList) {
        examineeDao.insertExamineeList(examineeList);
    }

    public Integer countExamineeByExamNumberAndClrNumber(String examNumber, String clrNumber) {
        return examineeDao.countExamineeByExamNumberAndClrNumber(examNumber, clrNumber);
    }

    public List<Integer> getSeatListByExamNumberAndClrNumber(String examNumber, String clrNumber) {
        return  examineeDao.getSeatListByExamNumberAndClrNumber(examNumber, clrNumber);
    }

    public void insertSingleExaminee(Examinee examinee) {
        examineeDao.insertSingleExaminee(examinee);
    }

    public Examinee getExamineeByUserNameAndExamNumber(String userName, String examNumber) {
        return examineeDao.getExamineeByUserNameAndExamNumber(userName, examNumber);
    }

    public List<Examinee> getExamineeListByNoPay(String userName) {
        return examineeDao.getExamineeListByNoPay(userName);
    }

    public Integer countExamineeNonPay(String userName) {
        return examineeDao.countExamineeNonPay(userName);
    }

    public void updatePaymentStatus(String userName, String examNumber) {
        examineeDao.updatePaymentStatus(userName, examNumber);
    }
}

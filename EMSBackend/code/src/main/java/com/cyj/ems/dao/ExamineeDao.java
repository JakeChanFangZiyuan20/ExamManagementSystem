package com.cyj.ems.dao;

import com.cyj.ems.domain.Examinee;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamineeDao {

    Examinee getExamineeByExamineeID(Integer examineeID);

    void insertExamineeList(@Param("examineeList") List<Examinee> examineeList);

    void insertSingleExaminee(Examinee examinee);

    Integer countExamineeByExamNumber(String examNumber);

    Integer countExamineeByUserName(String userName);

    Integer countExamineeByExamNumberAndClrNumber(String examNumber, String clrNumber);

    List<Examinee> getExamineeListByExamNumber(String examNumber);

    List<Examinee> getExamineeListByUserName(String userName);

    List<Examinee> getExamineeByExamineeNumber(String examineeNumber);

    List<Integer> getSeatListByExamNumberAndClrNumber(String examNumber, String clrNumber);

    Examinee getExamineeByUserNameAndExamNumber(String userName, String examNumber);

    List<Examinee> getExamineeListByNoPay(String userName);

    Integer countExamineeNonPay(String userName);

    void updatePaymentStatus(String userName, String examNumber);
}

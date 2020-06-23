package com.cyj.ems.dao;

import com.cyj.ems.domain.ClrMg;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClrMgDao {

    List<ClrMg> getClrMgByName(String clrMgName);

    List<ClrMg> getClrMgByClrID(Integer clrID);

    Integer countClrMgByClrID(Integer clrID);

    ClrMg hasClrMg(Integer clrID, String mgName, String mgPhone);

    void deleteClrMg(Integer clrID, String mgName, String mgPhone);

    void insertClrMg(ClrMg clrMg);

    void insertClrMgList(@Param("clrMgList") List<ClrMg> clrMgList);

    void updateClrMg(Integer clrID, String orgMgName, String newMgName, String orgMgPhone, String newMgPhone);

}

package com.cyj.ems.dao;

import com.cyj.ems.domain.Classroom;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClrDao {

    Classroom getClassroomByClrNumber(String clrNumber);

    Integer getClrIDByClrNumber(String clrNumber);

    List<Classroom> getAllClr();

    Integer countClr();

    void insertClassroom(Classroom classroom);

    Integer getClrCapacityByClrNumberList(@Param("clrNumberList") List<String> clrNumberList);

    List<Classroom> getClrListByClrNumberList(@Param("clrNumberList") List<String> clrNumberList);

}

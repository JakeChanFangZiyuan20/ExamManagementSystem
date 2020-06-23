package com.cyj.ems.service;

import com.cyj.ems.dao.ClrDao;
import com.cyj.ems.domain.Classroom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("clrService")
public class ClrService {

    @Autowired
    private ClrDao clrDao;

    public Classroom getClassroomByClrNumber(String clrNumber) {
        return clrDao.getClassroomByClrNumber(clrNumber);
    }

    public List<Classroom> getAllClr() {
        return clrDao.getAllClr();
    }

    public Integer getClrIDByClrNumber(String clrNumber) {
        return clrDao.getClrIDByClrNumber(clrNumber);
    }

    public Integer countClr() {
        return clrDao.countClr();
    }

    public boolean hasClassroom(String clrNumber) {
        Classroom classroom = getClassroomByClrNumber(clrNumber);
        return classroom != null;
    }

    public void insertClassroom(Classroom classroom) {
        clrDao.insertClassroom(classroom);
    }

    public Integer getClrCapacityByClrNumberList(List<String> clrNumberList) {
        Integer result = 0;
        if(clrNumberList.size() > 0){
            result = clrDao.getClrCapacityByClrNumberList(clrNumberList);
        }
        return result;
    }

    public List<Classroom> getClrListByClrNumberList(List<String> clrNumberList) {
        return clrDao.getClrListByClrNumberList(clrNumberList);
    }

}

package com.cyj.ems.service;

import com.cyj.ems.dao.UserDao;
import com.cyj.ems.domain.User;
import com.cyj.ems.vo.UpdateUserInfoVO;
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

@Service("userService")
public class UserService {

    @Autowired
    private UserDao userDao;

    public User getUserByUserName(String userName) {
        return userDao.getUserByUserName(userName);
    }

    public User verifyAndGetUser(String userName, String password) {
        User user = userDao.getUserByUserName(userName);
        if (user == null) return null;
        else if (user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public boolean verifyPwd(String userName, String inputPwd) {
        return inputPwd.equals(userDao.getPwdByUserName(userName));
    }

    public void updatePhoto(String userName, String photoPath) {
        userDao.updatePhoto(userName, photoPath);
    }

    public void updatePwd(String userName, String newPwd) {
        userDao.updatePwd(userName, newPwd);
    }

    public User updateUserInfo(UpdateUserInfoVO updateUserInfoVO) {
        // update trueName gender birthday identity phone email
        User updateUser = userDao.getUserByUserName(updateUserInfoVO.getUserName());
        updateUser.setTrueName(updateUserInfoVO.getTrueName());
        updateUser.setGender(updateUserInfoVO.getGender());
        updateUser.setBirthday(updateUserInfoVO.getBirthday());
        updateUser.setIdentity(updateUserInfoVO.getIdentity());
        updateUser.setPhone(updateUserInfoVO.getPhone());
        updateUser.setEmail(updateUserInfoVO.getEmail());

        userDao.updateUserInfo(updateUser);

        return updateUser;
    }

    public boolean hasUserName(String userName){
        return userDao.getUserByUserName(userName) != null;
    }

    public void insertSingleExamineeAccount(User user){
        userDao.insertSingleExamineeAccount(user);
    }

    public List<User> getExamineeAccountListFromUploadFile(String fileName) {
        InputStream fileInputStream = null;
        List<User> userList = new ArrayList<>();

        try {
            fileInputStream = new FileInputStream(fileName);
            HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream); // HSSFWorkBook for xls
            HSSFSheet sheet = workbook.getSheet("Sheet1");
            int rowNumber = sheet.getPhysicalNumberOfRows();
            for(int i = 1; i < rowNumber; i++) {
                Row row = sheet.getRow(i);

                User user = new User();

                // col 1
                Cell cell01 = row.getCell(0);
                user.setUserName(cell01.getStringCellValue());
                if(user.getUserName().length() == 0) break;

                // col 2
                Cell cell02 = row.getCell(1);
                user.setPassword(cell02.getStringCellValue());

                user.setRole(3);
                userList.add(user);
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

        return userList;
    }

    public List<String> findHaveExistedList(List<String> userNameList) {
        if(userNameList.size() == 0) return new ArrayList<>();
        return userDao.haveExistedList(userNameList);
    }

    public void insertExamineeAccountList(List<User> examineeAccountList){
        userDao.insertExamineeAccountList(examineeAccountList);
    }

    public List<User> getUserListByUserNameList(List<String> userNameList){
        return userDao.getUserListByUserNameList(userNameList);
    }

    public void insertSingleUser(User user) {
        userDao.insertSingleUser(user);
    }
}

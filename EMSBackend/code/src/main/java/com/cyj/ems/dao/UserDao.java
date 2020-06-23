package com.cyj.ems.dao;

import com.cyj.ems.domain.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao {

    User getUserByUserName(String userName);

    String getPwdByUserName(String userName);

    void updatePhoto(String userName, String photoPath);

    void updatePwd(String userName, String newPwd);

    void updateUserInfo(User user); // trueName gender birthday identity phone email

    void insertSingleExamineeAccount(User user);

    List<String> haveExistedList(@Param("userNameList") List<String> userNameList);

    void insertExamineeAccountList(@Param("examineeAccountList") List<User> examineeAccountList);

    List<User> getUserListByUserNameList(@Param("userNameList") List<String> userNameList);

    void insertSingleUser(User user);
}

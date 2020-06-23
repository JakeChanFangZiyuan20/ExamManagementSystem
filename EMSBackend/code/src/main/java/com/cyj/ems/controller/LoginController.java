package com.cyj.ems.controller;

import com.cyj.ems.domain.User;
import com.cyj.ems.dto.UserInfoDTO;
import com.cyj.ems.service.UserService;
import com.cyj.ems.vo.LoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @CrossOrigin
    @PostMapping("/api/login") // no filter & interceptor
    @ResponseBody
    public UserInfoDTO login(@RequestBody LoginVO loginVO, HttpServletRequest request) {
        String userName = loginVO.getUserName();
        String password = loginVO.getPassword();

        User user = userService.verifyAndGetUser(userName, password);
        if(user == null){
            return null;
        }

        // session 设置
        HttpSession session = request.getSession();
        String sessionID = session.getId();

        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setSessionID(sessionID);
        userInfoDTO.setUserName(loginVO.getUserName());
        userInfoDTO.setRole(user.getRole());
        userInfoDTO.setTrueName(user.getTrueName());
        userInfoDTO.setGender(user.getGender());
        userInfoDTO.setBirthday(user.getBirthday());
        userInfoDTO.setIdentity(user.getIdentity());
        userInfoDTO.setPhone(user.getPhone());
        userInfoDTO.setEmail(user.getEmail());
        userInfoDTO.setPhotoURL(user.getPhotoURL());

        session.setAttribute(sessionID, userInfoDTO.toString());

        return userInfoDTO;
    }
}

package com.cyj.ems.controller;

import com.cyj.ems.domain.User;
import com.cyj.ems.dto.UploadFileResultDTO;
import com.cyj.ems.dto.NormalResultDTO;
import com.cyj.ems.dto.UserInfoDTO;
import com.cyj.ems.service.UserService;
import com.cyj.ems.vo.ModifyPwdVO;
import com.cyj.ems.vo.UpdateUserInfoVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;

@Controller
public class EditInfoController {

    @Value("${localfilepath.images.images}")
    private String imagesPath;

    @Resource
    private UserService userService;

    @Resource
    RedisTemplate<String, Object> redisTemplate;

    @CrossOrigin
    @PostMapping("/api/editPhoto") // no filter & interceptor
    @ResponseBody
    public UploadFileResultDTO editPhoto(@RequestParam("file") MultipartFile photoFile,
                                         @RequestParam("sessionID") String sessionID,
                                         @RequestParam("userName") String userName) {
        UploadFileResultDTO uploadFileResultDTO = new UploadFileResultDTO();

        // 检验登录状态
        String keyName = "spring:session:sessions:" + sessionID;
        String hashKey = "sessionAttr:" + sessionID;
        if(!redisTemplate.opsForHash().hasKey(keyName, hashKey)){
//            System.out.println("not login");
            uploadFileResultDTO.setLogin(false);
            uploadFileResultDTO.setUpload(false);
            return uploadFileResultDTO;
        }
        // 已登录在线
        uploadFileResultDTO.setLogin(true);

        // 保存并修改数据库
        String saveFileName = userName + ".jpg";
        String savePath = imagesPath;
        try {
//            System.out.println("saving");
            photoFile.transferTo(new File(savePath + saveFileName));
            userService.updatePhoto(userName, "/images/" + saveFileName);
            uploadFileResultDTO.setUpload(true);
            uploadFileResultDTO.setPhotoURL("/images/" + saveFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return uploadFileResultDTO;
    }

    @CrossOrigin
    @PostMapping("/api/verifyPwd") // need filter and interceptor
    @ResponseBody
    public NormalResultDTO verifyPwd(@RequestBody ModifyPwdVO modifyPwdVO){
//        System.out.println(modifyPwdVO.toString());

        String userName = modifyPwdVO.getUserName();
        String pwd = modifyPwdVO.getPwd();

        NormalResultDTO normalResultDTO = new NormalResultDTO();
        if(!userService.verifyPwd(userName, pwd)){
            normalResultDTO.setStatus(400); // 400 代表密码错误
            return normalResultDTO;
        }
        normalResultDTO.setStatus(200); // 200 代表密码正确
        return normalResultDTO;
    }

    @CrossOrigin
    @PostMapping("/api/changePwd") // need filter and interceptor
    @ResponseBody
    public NormalResultDTO changePwd(@RequestBody ModifyPwdVO modifyPwdVO) {
        String userName = modifyPwdVO.getUserName();
        String newPwd = modifyPwdVO.getPwd();

        userService.updatePwd(userName, newPwd);

        NormalResultDTO normalResultDTO = new NormalResultDTO();
        normalResultDTO.setStatus(200);

        return normalResultDTO;
    }

    @CrossOrigin
    @PostMapping("/api/updateUserInfo") // need filter and interceptor
    @ResponseBody
    public UserInfoDTO updateUserInfo(@RequestBody UpdateUserInfoVO updateUserInfoVO) {
//        System.out.println(updateUserInfoVO.toString());

        User newUser = userService.updateUserInfo(updateUserInfoVO);

        UserInfoDTO newUserInfoDTO = new UserInfoDTO();
        newUserInfoDTO.setSessionID(updateUserInfoVO.getSessionID());
        newUserInfoDTO.setUserName(updateUserInfoVO.getUserName());
        newUserInfoDTO.setRole(newUser.getRole());
        newUserInfoDTO.setPhotoURL(newUser.getPhotoURL());

        newUserInfoDTO.setTrueName(newUser.getTrueName());
        newUserInfoDTO.setGender(newUser.getGender());
        newUserInfoDTO.setBirthday(newUser.getBirthday());
        newUserInfoDTO.setIdentity(newUser.getIdentity());
        newUserInfoDTO.setPhone(newUser.getPhone());
        newUserInfoDTO.setEmail(newUser.getEmail());

//        System.out.println(newUserInfoDTO.toString());

        return newUserInfoDTO;
    }

}

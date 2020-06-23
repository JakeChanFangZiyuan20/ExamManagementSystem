package com.cyj.ems.controller;

import com.cyj.ems.domain.User;
import com.cyj.ems.service.UserService;
import com.cyj.ems.utils.UploadAndDownload;
import com.fasterxml.jackson.core.json.async.NonBlockingJsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
public class AddExamineeController {

    @Value("${localfilepath.formatfile.addexaminee}")
    private String formatFileAddExamineePath;

    @Value("${localfilepath.temp.addexaminee}")
    private String tempFileAddExamineePath;

    @Autowired
    UserService userService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @CrossOrigin
    @PostMapping("/api/addSingleExamineeAccount") // need filter and interceptor
    @ResponseBody
    public Map<String, Object> addSingleExamineeAccount(@RequestBody Map<String, Object> requestMap) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("status", 200);

        if(userService.hasUserName(requestMap.get("newUserName").toString())) {
            responseMap.replace("status", 401);
            return responseMap;
        }

        User newUser = new User();
        newUser.setUserName(requestMap.get("newUserName").toString());
        newUser.setPassword(requestMap.get("password").toString());
        Integer role = Integer.parseInt(requestMap.get("role").toString());
        newUser.setRole(role);

        userService.insertSingleExamineeAccount(newUser);

        return responseMap;
    }

    @CrossOrigin
    @GetMapping("/api/downloadExamineeAccountFormatFile") // not need filter and interceptor
    public void downloadExamineeAccountFormatFile(HttpServletResponse response) {
        String downloadFileName = "格式文件-考生账号列表.xls";
        String downloadFilePath = formatFileAddExamineePath;
        response = UploadAndDownload.download(downloadFilePath, downloadFileName, response);
    }

    @CrossOrigin
    @PostMapping("/api/uploadXLSFileFromAddExaminee") // no need filter and interceptor
    @ResponseBody
    public Map<String, Object> uploadXLSFileFromAddExaminee(@RequestParam("file") MultipartFile xlsFile,
                                                            @RequestParam("sessionID") String sessionID,
                                                            @RequestParam("userName") String userName){
        String fileName = tempFileAddExamineePath + userName + ".xls";
        return UploadAndDownload.uploadFile(redisTemplate, xlsFile, sessionID, fileName);
    }

    @CrossOrigin
    @PostMapping("/api/removeUploadFileFromAddExaminee") // need filter and interceptor
    @ResponseBody
    public Map<String, Object> removeUploadFile(@RequestBody Map<String, Object>requestMap) {
        String filePath = tempFileAddExamineePath;
        String fileName = requestMap.get("userName") + ".xls";
        return UploadAndDownload.removeUploadFile(filePath, fileName);
    }

    @CrossOrigin
    @PostMapping("/api/insertExamineeAccountList") // need filter and interceptor
    @ResponseBody
    public Map<String, Object> insertExamineeAccountList(@RequestBody Map<String, Object>requestMap) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("status", 200);

        String filePath = tempFileAddExamineePath;
        String fileName = requestMap.get("userName").toString() + ".xls";
        List<User> examineeAccountList = userService.getExamineeAccountListFromUploadFile(filePath + fileName);

        Map<String, String> userNameMap = new HashMap<>();
        List<String> userNameList = new ArrayList<>();
        for(User examineeUser : examineeAccountList) {
            userNameList.add(examineeUser.getUserName());
            userNameMap.put(examineeUser.getUserName(), examineeUser.getPassword());
        }

        List<String> haveExistedList = userService.findHaveExistedList(userNameList);
        if(haveExistedList.size() != 0){
            for(String userNameStr : haveExistedList) {
                userNameMap.remove(userNameStr);
            }
        }

        examineeAccountList = new ArrayList<>();
        for(Map.Entry<String, String> entry : userNameMap.entrySet()) {
            User newExamineeUser = new User();
            newExamineeUser.setUserName(entry.getKey());
            newExamineeUser.setPassword(entry.getValue());
            newExamineeUser.setRole(3);
            examineeAccountList.add(newExamineeUser);
        }

        userService.insertExamineeAccountList(examineeAccountList);

        return responseMap;
    }
}

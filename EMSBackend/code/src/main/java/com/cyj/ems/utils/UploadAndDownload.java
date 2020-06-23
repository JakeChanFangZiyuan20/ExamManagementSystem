package com.cyj.ems.utils;

import org.apache.commons.io.IOUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class UploadAndDownload {

    private UploadAndDownload(){}

    static public HttpServletResponse download(String downloadFilePath, String downloadFileName,
                                               HttpServletResponse response) {
        File file = new File(downloadFilePath +  downloadFileName);

        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        try {
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(downloadFileName, "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.addHeader("Content-Length", String.valueOf(file.length()));

        InputStream fileInputStream = null;
        OutputStream outputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            outputStream = response.getOutputStream();

            IOUtils.copy(fileInputStream, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(fileInputStream != null){
                    fileInputStream.close();
                }
                if(outputStream != null){
                    outputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return response;
    }

    static public Map<String, Object> uploadFile(RedisTemplate<String, Object> redisTemplate,
                                                 MultipartFile xlsFile,String sessionID,
                                                 String saveFileName) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("isLogin", true);
        responseMap.put("isUpload", true);

        // 检验登录状态
        String keyName = "spring:session:sessions:" + sessionID;
        String hashKey = "sessionAttr:" + sessionID;
        if(!redisTemplate.opsForHash().hasKey(keyName, hashKey)){
            System.out.println("not login");
            responseMap.replace("isLogin", false);
            responseMap.replace("isUpload", false);
            return responseMap;
        }
        // 已登录在线
        responseMap.replace("isLogin", true);

        // 保存到临时文件夹
        File file = new File(saveFileName);
        if(file.exists()) {
            file.delete();
        }
        try {
//            System.out.println("saving");
            xlsFile.transferTo(new File(saveFileName));
            responseMap.replace("isUpload", true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return responseMap;
    }

    static public Map<String, Object> removeUploadFile(String filePath, String fileName) {
        Map<String, Object> responseMap = new HashMap<>();
        File file = new File(filePath + fileName);
        if(file.exists()) {
            file.delete();
        }
        responseMap.put("status", 200);
        return responseMap;
    }

}

package com.cyj.ems.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cyj.ems.domain.AlipayBean;
import com.cyj.ems.domain.Examinee;
import com.cyj.ems.domain.User;
import com.cyj.ems.utils.AlipayUtils;
import com.cyj.ems.utils.RegExaminee;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Service("alipayService")
public class AlipayService {

    @Value("${localfilepath.images.images}")
    private String imagesPath;

    @Value("${localfilepath.images.temp}")
    private String imagesTempPath;

    @Autowired
    UserService userService;

    @Autowired
    ExamineeService examineeService;

    @Resource
    RedisTemplate<String, Object> redisTemplate;

    public String payService(AlipayBean alipayBean) {
        return AlipayUtils.connect2Pay(alipayBean);
    }

    public String sync(HttpServletRequest request) {
        return "sync";
    }

    public String notify(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> paramsMap = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for(Map.Entry<String, String[]> entry : requestParams.entrySet()) {
            String key = entry.getKey();
            String[] values = entry.getValue();
            String valueStr = "";
            for(int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            paramsMap.put(key, valueStr);
        }

        String body = paramsMap.get("body").toString();
        if(body.equals("role2")){
            RegExaminee regExaminee = getRegExamineeFromSessionID(paramsMap.get("out_trade_no"));
            regExaminee.setPaymentStatus(1);

            User user = setUser(regExaminee);
            userService.insertSingleUser(user);
            Examinee examinee = setExaminee(regExaminee);
            examineeService.insertSingleExaminee(examinee);
        } else if(body.equals("role3")) {
            Examinee examinee = getExamineeFromSessionID(paramsMap.get("out_trade_no"));
            examinee.setPaymentStatus(1);
            examineeService.insertSingleExaminee(examinee);
        } else if(body.equals("role31")) {
            String basicInfo = paramsMap.get("out_trade_no");
            Integer split1Index = basicInfo.indexOf(':');
            String userName = basicInfo.substring(0, split1Index);
            Integer split2Index = basicInfo.indexOf('-');
            String examNumber = basicInfo.substring(split1Index + 1, split2Index);
            examineeService.updatePaymentStatus(userName, examNumber);
        }
        return "success";
    }

    private RegExaminee getRegExamineeFromSessionID(String sessionID) {
        String keyName = "spring:session:sessions:" + sessionID;
        Map<Object, Object> content = redisTemplate.opsForHash().entries(keyName);
        String regExamineeStrObj = (String) content.get("sessionAttr:" + sessionID);
        Integer startIndex = regExamineeStrObj.indexOf('{');
        regExamineeStrObj = regExamineeStrObj.substring(startIndex, regExamineeStrObj.length());
        JSONObject jsonObject = JSON.parseObject(regExamineeStrObj);
        return JSONObject.toJavaObject(jsonObject, RegExaminee.class);
    }

    private User setUser(RegExaminee regExaminee) {
        User user = new User();
        user.setUserName(regExaminee.getUserName());
        user.setPassword(regExaminee.getPassword());
        user.setRole(regExaminee.getRole());
        user.setTrueName(regExaminee.getTrueName());
        user.setGender(regExaminee.getGender());
        user.setBirthday(regExaminee.getBirthday());
        user.setIdentity(regExaminee.getIdentity());
        user.setPhone(regExaminee.getPhone());
        user.setEmail(regExaminee.getEmail());
        user.setPhotoURL("/images/" + user.getUserName() + ".jpg");
        // 图象转移
        String org = imagesTempPath + user.getUserName() + ".jpg";
        String dest = imagesPath + user.getUserName() + ".jpg";
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(new File(org));
            outputStream = new FileOutputStream(new File(dest));

            IOUtils.copy(inputStream, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return user;
    }

    private Examinee setExaminee(RegExaminee regExaminee) {
        Examinee examinee = new Examinee();
        examinee.setExamineeNumber(regExaminee.getExamineeNumber());
        examinee.setUserName(regExaminee.getUserName());
        examinee.setExamNumber(regExaminee.getExamNumber());
        examinee.setPaymentStatus(regExaminee.getPaymentStatus());
        examinee.setClrNumber(regExaminee.getClrNumber());
        examinee.setSeatNumber(regExaminee.getSeatNumber());

        return examinee;
    }

    private Examinee getExamineeFromSessionID(String sessionID) {
        String keyName = "spring:session:sessions:" + sessionID;
        Map<Object, Object> content = redisTemplate.opsForHash().entries(keyName);
        String examineeStrObj = (String) content.get("sessionAttr:" + sessionID);
        Integer startIndex = examineeStrObj.indexOf('{');
        examineeStrObj = examineeStrObj.substring(startIndex, examineeStrObj.length());
        JSONObject jsonObject = JSON.parseObject(examineeStrObj);
        return JSONObject.toJavaObject(jsonObject, Examinee.class);
    }
}

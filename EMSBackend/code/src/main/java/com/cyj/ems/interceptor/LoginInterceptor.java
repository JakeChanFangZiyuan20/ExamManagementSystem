package com.cyj.ems.interceptor;

import com.alibaba.fastjson.JSON;
import com.cyj.ems.dto.NormalResultDTO;
import com.cyj.ems.utils.RequestWrapper;
import com.cyj.ems.vo.NormalRequestVO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Resource
    RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        System.out.println("interceptor");
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:8080");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE, PATCH");
        response.setHeader("Access-Control-Allow-Headers", "DNT,X-CustomHeader,Keep-Alive,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Content-Range,Range");

        RequestWrapper requestWrapper = new RequestWrapper(request);
        String requestBodyInfo = requestWrapper.getBody();
//        System.out.println(requestBodyInfo);
        NormalRequestVO normalRequestVO = JSON.parseObject(requestBodyInfo, NormalRequestVO.class);
        if(normalRequestVO == null) return false;

        String requestSessionID = normalRequestVO.getSessionID();

        String keyName = "spring:session:sessions:" + requestSessionID;
        String hashKey = "sessionAttr:" + requestSessionID;
//        System.out.println(redisTemplate.opsForHash().hasKey(keyName, hashKey));

        return redisTemplate.opsForHash().hasKey(keyName, hashKey);
//        return true;
    }

}

package com.cyj.ems.config;

import com.cyj.ems.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class MyWebMVCConfig implements WebMvcConfigurer {

    @Resource
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor).addPathPatterns("/**")
                .excludePathPatterns("/api/login", "/images/**",
                        "/api/editPhoto", "/api/downloadClrMgList",
                        "/api/uploadXLSFile", "/api/downloadFormatFileFromAddExam",
                        "/api/uploadXLSFileFromAddExam", "/api/downloadExamineeAccountFormatFile",
                        "/api/uploadXLSFileFromAddExaminee", "/api/downloadExamInfoList", "/api/downloadClrMgInfoList",
                        "/api/downloadExamMgList", "/api/downloadExamClrList", "/api/downloadExamineeList",
                        "/api/uploadAvatar", "/api/notifyUrl", "/api/returnUrl");
    }

}

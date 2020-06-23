package com.cyj.ems.filter;

import com.cyj.ems.utils.RequestWrapper;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter(urlPatterns = {"/api/verifyPwd", "/api/changePwd", "/api/updateUserInfo",
        "/api/removeUploadFile", "/api/insertClrMgList", "/api/removeUploadFileFromAddExam",
        "/api/addExam", "/api/addSingleExamineeAccount", "/api/removeUploadFileFromAddExaminee",
        "/api/insertExamineeAccountList", "/api/searchExam", "/api/searchExamMg",
        "/api/searchExamClr", "/api/searchExaminee","/api/searchClr",
        "/api/searchClrMg", "/api/deleteClrMg", "/api/updateClrMg",
        "/api/searchExamineeFromViewExaminee", "/api/searchExamListFromViewExaminee", "/api/buildExamInfoList",
        "/api/exportClrMgList", "/api/exportExamMgList", "/api/exportExamClrList",
        "/api/exportExamineeList", "/api/viewCertification", "/api/viewExamClrCheck",
        "/api/viewExamineeCheck", "/api/locateExam", "/api/getExamClrList",
        "/api/addInitAccount", "/api/redisSaveRegExaminee" , "/api/payRegPrice",
        "/api/checkInfoComplete", "/api/isAttended", "/api/processAttend",
        "/api/searchExamFromViewRegPrice", "/api/payInfo2Redis"},
        filterName = "loginFilter")
public class LoginFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        System.out.println("filter");
        ServletRequest requestWrapper = null;
        if (request instanceof HttpServletRequest) {
            requestWrapper = new RequestWrapper((HttpServletRequest) request);
        }
        if (requestWrapper == null) {
            chain.doFilter(request, response);
        } else {
            chain.doFilter(requestWrapper, response);
        }
    }

    @Override
    public void destroy() {

    }
}

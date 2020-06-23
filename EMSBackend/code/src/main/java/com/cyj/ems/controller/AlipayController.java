package com.cyj.ems.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.cyj.ems.config.AlipayConfig;
import com.cyj.ems.domain.AlipayBean;
import com.cyj.ems.service.AlipayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
public class AlipayController {

    @Autowired
    AlipayService alipayService;

    @CrossOrigin
    @PostMapping("/api/payRegPrice") // need filter and interceptor
    @ResponseBody
    public String payRegPrice(@RequestBody Map<String, Object> requestMap, HttpServletRequest request, HttpServletResponse response) {
        Integer role = (Integer) requestMap.get("role");
        if(role == 2) {
            AlipayConfig.return_url = "http://localhost:8080/mgPaySuccess";
        } else {
            AlipayConfig.return_url = "http://localhost:8080/examineePaySuccess";
        }

        AlipayBean alipayBean = new AlipayBean();
        alipayBean.setOut_trade_no(requestMap.get("out_trade_no").toString());
        alipayBean.setSubject(requestMap.get("subject").toString());
        alipayBean.setTotal_amount(requestMap.get("total_amount").toString());
        if(role == 2) alipayBean.setBody("role2");
        else if(role == 3) alipayBean.setBody("role3");
        else if(role == 31) alipayBean.setBody("role31");


        return alipayService.payService(alipayBean);
    }

    @PostMapping("/api/notifyUrl")
    private String alipayNotify(HttpServletRequest request, HttpServletResponse response) {
        return alipayService.notify(request, response);
    }

    @GetMapping("/api/returnUrl")
    private void alipayReturnUrl(HttpServletRequest request, HttpServletResponse response) {
        alipayService.sync(request);
    }


}

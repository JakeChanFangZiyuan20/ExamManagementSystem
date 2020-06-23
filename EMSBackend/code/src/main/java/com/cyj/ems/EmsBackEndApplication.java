package com.cyj.ems;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
@MapperScan("com.cyj.ems.dao")
public class EmsBackEndApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmsBackEndApplication.class, args);
    }

}

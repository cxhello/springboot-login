package com.cxhello.login;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.cxhello.login.dao")
public class SpringbootLoginApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootLoginApplication.class, args);
    }

}

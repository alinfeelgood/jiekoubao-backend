package com.huanlin.apifrontinterface;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.huanlin.apifrontinterface.mapper")
public class ApiFrontinterfaceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiFrontinterfaceApplication.class, args);
    }

}

package com.huanlin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class ApiSmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiSmsApplication.class, args);
    }

}

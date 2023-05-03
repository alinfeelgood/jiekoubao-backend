package com.huanlin.apigateway;

import com.huanlin.project.provider.DemoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

@EnableDubbo
@Service
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ApiGatewayApplication {
    @DubboReference
    private DemoService demoService;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ApiGatewayApplication.class, args);
        ApiGatewayApplication application = context.getBean(ApiGatewayApplication.class);
        String res = application.doSayHello("world");
        System.out.println(res);
    }
   public  String doSayHello(String name){
        return demoService.sayHello(name);
   }

}

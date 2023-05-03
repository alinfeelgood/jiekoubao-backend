package com.huanlin.project.test;

import com.huanlin.apiclientsdk.client.ApiClient;
import com.huanlin.apiclientsdk.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class ApiFrontinterfaceTests {
   @Resource
   private ApiClient client;
    @Test
    void contextLoads() {
        String res1 = client.getNameByGet("huanlin");
        User user = new User();
        user.setUsername("alin");
        String res2 = client.getUserByPost(user);
        System.out.println(res1);
        System.out.println(res2);
    }

}

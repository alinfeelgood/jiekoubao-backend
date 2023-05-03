package com.huanlin.project.service;

import com.hunalin.service.InnerUserInterfaceInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;


@SpringBootTest
public class UserInterfaceInfoServiceTest {
    @Autowired
    private UserInterfaceInfoService userInterfaceInfoService;
    @Resource
    private InnerUserInterfaceInfoService invokeCount;
    @Test
    public void addInterfaceCount() {
        boolean b = invokeCount.addInterfaceCount(1, 1640946676522340354L);
        System.out.println(b);
    }
}
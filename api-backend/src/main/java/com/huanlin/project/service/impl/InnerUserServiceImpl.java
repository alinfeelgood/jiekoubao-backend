package com.huanlin.project.service.impl;

import com.hunalin.model.entity.User;
import com.hunalin.service.InnerUserService;
import com.huanlin.project.service.UserService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
@DubboService
public class InnerUserServiceImpl implements InnerUserService {
    @Autowired
    UserService userService;
    @Override
    public User getInvokeUser(String accessKey) {
        User user = userService.getInvokeUser(accessKey);
        return user;
    }
}

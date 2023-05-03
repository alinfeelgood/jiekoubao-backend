package com.huanlin.project.service.impl;

import com.hunalin.model.entity.InterfaceInfo;
import com.hunalin.service.InnerInterfaceInfoService;
import com.huanlin.project.service.InterfaceInfoService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
@DubboService
public class InnerInterfaceInfoServiceImpl implements InnerInterfaceInfoService {
    @Autowired
    private InterfaceInfoService service;
    @Override
    public InterfaceInfo getInterfaceInfo(String path, String method) {
        InterfaceInfo interfaceInfo = service.getInterfaceInfo(path, method);
        return  interfaceInfo;
    }
}

package com.huanlin.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hunalin.model.entity.UserInterfaceInfo;
import com.hunalin.service.InnerUserInterfaceInfoService;
import com.huanlin.project.common.ErrorCode;
import com.huanlin.project.exception.BusinessException;
import com.huanlin.project.mapper.UserInterfaceInfoMapper;
import com.huanlin.project.service.UserInterfaceInfoService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
@DubboService
public class InnerUserInterfaceInfoServiceImpl implements InnerUserInterfaceInfoService {
    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;
    @Resource
    UserInterfaceInfoMapper mapper;
    @Override
    public boolean addInterfaceCount(long interfaceInfoId, long userId) {
        boolean isSuccess = userInterfaceInfoService.addInterfaceCount(interfaceInfoId, userId);
        return isSuccess;
    }

    @Override
    public int getInterfaceLeftCount(long interfaceInfoId,long userId) {
        QueryWrapper<UserInterfaceInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("interfaceInfoId",interfaceInfoId);
        wrapper.eq("userId",userId);
        UserInterfaceInfo userInterfaceInfo = mapper.selectOne(wrapper);
        if(userInterfaceInfo == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        Integer leftNum = userInterfaceInfo.getLeftNum();
        return leftNum;
    }
}

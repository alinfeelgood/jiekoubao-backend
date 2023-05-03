package com.huanlin.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huanlin.project.model.vo.UserInfoVO;
import com.hunalin.model.entity.InterfaceInfo;
import com.hunalin.model.entity.User;

import java.util.List;

/**
 *
 */
public interface InterfaceInfoService extends IService<InterfaceInfo> {

     void validInterfaceInfo(InterfaceInfo InterfaceInfo, boolean add);

     InterfaceInfo getInterfaceInfo(String path, String method);

     List<Long> selectApiId();

    UserInfoVO getUserInfo(User loginUser);
}

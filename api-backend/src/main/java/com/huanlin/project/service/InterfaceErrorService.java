package com.huanlin.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hunalin.model.entity.InterfaceError;
import com.hunalin.model.entity.InterfaceInfo;

import java.util.List;

/**
 *
 */
public interface InterfaceErrorService extends IService<InterfaceError> {

    List<InterfaceError> getErrorInterfaceList();

    void validInterfaceError(InterfaceError interfaceError, boolean add);
}

package com.huanlin.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huanlin.project.common.ErrorCode;
import com.huanlin.project.exception.BusinessException;
import com.huanlin.project.exception.ThrowUtils;
import com.huanlin.project.mapper.InterfaceErrorMapper;
import com.huanlin.project.service.InterfaceErrorService;
import com.hunalin.model.entity.InterfaceError;
import com.hunalin.model.entity.InterfaceInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 */
@Service
public class InterfaceErrorServiceImpl extends ServiceImpl<InterfaceErrorMapper, InterfaceError>
    implements InterfaceErrorService {

    @Override
    public void validInterfaceError(InterfaceError interfaceError, boolean add) {
        if (interfaceError == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String email = interfaceError.getEmail();
        String information = interfaceError.getInformation();
        String name = interfaceError.getName();
        Long interfaceId = interfaceError.getInterfaceId();
        // 创建时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(StringUtils.isAnyBlank(name, email, information), ErrorCode.PARAMS_ERROR);
        }
        // 有参数则校验
        if (StringUtils.isNotBlank(name) && name.length() > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口名称过长");
        }
        if (interfaceId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口id不能为空");
        }
    }

    @Override
    public List<InterfaceError> getErrorInterfaceList() {
        QueryWrapper<InterfaceError> wrapper = new QueryWrapper<>();
        wrapper.eq("status",0);
        wrapper.orderByDesc("createTime");
        List<InterfaceError> interfaceErrors = this.baseMapper.selectList(wrapper);
        return interfaceErrors;
    }

}





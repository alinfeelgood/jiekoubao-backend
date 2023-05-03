package com.huanlin.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.huanlin.project.annotation.AuthCheck;
import com.huanlin.project.common.BaseResponse;
import com.huanlin.project.common.ErrorCode;
import com.huanlin.project.common.ResultUtils;
import com.huanlin.project.exception.BusinessException;
import com.huanlin.project.model.dto.errorinterface.InterfaceErrorAddRequest;
import com.huanlin.project.model.dto.errorinterface.InterfaceErrorUpdateRequest;
import com.huanlin.project.service.InterfaceErrorService;
import com.hunalin.model.entity.InterfaceError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/interfaceError")
@Slf4j
public class InterfaceErrorController {
    @Autowired
    InterfaceErrorService interfaceErrorService;

    /**
     * 保存接口异常反馈页
     * @param errorAddRequest
     * @return
     */
    @PostMapping("/save")
    public BaseResponse<Boolean> saveErrorInformation(@RequestBody InterfaceErrorAddRequest errorAddRequest){
            if(errorAddRequest == null){
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"请输入完整的数据");
            }
        InterfaceError interfaceError = new InterfaceError();
        BeanUtils.copyProperties(errorAddRequest,interfaceError);

        boolean isSuccess = interfaceErrorService.save(interfaceError);
            if(!isSuccess){
                throw  new  BusinessException(ErrorCode.OPERATION_ERROR,"请重新提交");
            }
            return ResultUtils.success(isSuccess);
    }

    /**
     * 查询所有未处理的异常接口信息
     * @return
     */
    @AuthCheck(mustRole = "admin")
    @GetMapping("/get")
    public BaseResponse<List<InterfaceError>> getErrorInformationByList(){
       List<InterfaceError> list = interfaceErrorService.getErrorInterfaceList();
       return ResultUtils.success(list);
    }

    /**
     * 根据接口id修改接口错误信息状态
     * @param id
     * @return
     */
    @AuthCheck(mustRole = "admin")
    @GetMapping("/update/status")
    @Transactional
    public BaseResponse<Boolean> updateErrorStatusById(Long id,String email,String information){
        if(id <= 0 || email == null || information == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"此异常数据不存在，修改状态失败");
        }
        QueryWrapper<InterfaceError> wrapper = new QueryWrapper<>();
        wrapper.eq("interfaceId",id);
        wrapper.eq("email",email);
        wrapper.eq("information",information);
        InterfaceError interfaceError = interfaceErrorService.getOne(wrapper);
        interfaceError.setStatus(1);
        boolean save = interfaceErrorService.updateById(interfaceError);
        if(!save){
            throw new  BusinessException(ErrorCode.OPERATION_ERROR,"修改状态失败");
        }
        return ResultUtils.success(save);
    }
}

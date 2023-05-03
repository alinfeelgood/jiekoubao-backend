package com.huanlin.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.huanlin.project.annotation.AuthCheck;
import com.huanlin.project.common.BaseResponse;
import com.huanlin.project.common.ErrorCode;
import com.huanlin.project.common.ResultUtils;
import com.huanlin.project.exception.BusinessException;
import com.huanlin.project.mapper.UserInterfaceInfoMapper;
import com.huanlin.project.model.vo.InterfaceInfoVO;
import com.huanlin.project.model.vo.UserInterfaceInfoVo;
import com.huanlin.project.service.InterfaceInfoService;
import com.huanlin.project.service.UserInterfaceInfoService;
import com.huanlin.project.service.UserService;
import com.hunalin.model.entity.InterfaceInfo;
import com.hunalin.model.entity.User;
import com.hunalin.model.entity.UserInterfaceInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/analysis")
@Slf4j
public class AnalysisController {
    @Resource
    private UserInterfaceInfoMapper userInterfaceInfoMapper;

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Resource
    private UserService userService;
    @Autowired
    private UserInterfaceInfoService userInterfaceInfoService;
//    @GetMapping("/top/interface/invoke")
//    @AuthCheck(mustRole = "admin")
//    public BaseResponse<List<UserInterfaceInfoVo>> getUserInterfaceTotalNums(long limit){
//     List<UserInterfaceInfoVo> res = userInterfaceInfoService.getUserInterfaceTotalNums(limit);
//     return ResultUtils.success(res);
//    }

    /**
     * 接口被调用次数TOP5
     * @return
     */
    @GetMapping("/top/interface/invoke")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<List<InterfaceInfoVO>> listTopInvokeInterfaceInfo() {
        List<UserInterfaceInfo> userInterfaceInfoList = userInterfaceInfoMapper.listTopInvokeInterfaceInfo(5);
        Map<Long, List<UserInterfaceInfo>> interfaceInfoIdObjMap = userInterfaceInfoList.stream()
                .collect(Collectors.groupingBy(UserInterfaceInfo::getInterfaceInfoId));
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", interfaceInfoIdObjMap.keySet());
        List<InterfaceInfo> list = interfaceInfoService.list(queryWrapper);
        if (CollectionUtils.isEmpty(list)) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        List<InterfaceInfoVO> interfaceInfoVOList = list.stream().map(interfaceInfo -> {
            InterfaceInfoVO interfaceInfoVO = new InterfaceInfoVO();
            BeanUtils.copyProperties(interfaceInfo, interfaceInfoVO);
            int totalNum = interfaceInfoIdObjMap.get(interfaceInfo.getId()).get(0).getTotalNum();
            interfaceInfoVO.setTotalNum(totalNum);
            return interfaceInfoVO;
        }).collect(Collectors.toList());
        return ResultUtils.success(interfaceInfoVOList);
    }

    /**
     * 最活跃的用户TOP5
     * @return
     */
    @GetMapping("/top/interface/user")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<List<UserInterfaceInfoVo>> listTopUserInvokeInterfaceInfo() {
        List<UserInterfaceInfo> userInterfaceInfoList = userInterfaceInfoMapper.listTopUserInvokeInterfaceInfo(5);
        Map<Long, List<UserInterfaceInfo>> userIdObjMap = userInterfaceInfoList.stream()
                .collect(Collectors.groupingBy(UserInterfaceInfo::getUserId));
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", userIdObjMap.keySet());
        List<User> list = userService.list(queryWrapper);
        if (CollectionUtils.isEmpty(list)) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        //list为userId的信息
        //todo 只需返回用户信息即可
        List<UserInterfaceInfoVo> interfaceInfoVOList = list.stream().map(user -> {
            UserInterfaceInfoVo userVO = new UserInterfaceInfoVo();
            BeanUtils.copyProperties(user, userVO);
            int totalNum = userIdObjMap.get(user.getId()).get(0).getTotalNum();
            userVO.setTotalNum(totalNum);
            return userVO;
        }).collect(Collectors.toList());
        return ResultUtils.success(interfaceInfoVOList);
    }
}

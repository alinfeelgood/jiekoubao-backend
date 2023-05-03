package com.huanlin.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huanlin.project.model.vo.UserInfoVO;
import com.huanlin.project.model.vo.UserInterface;
import com.huanlin.project.service.UserInterfaceInfoService;
import com.hunalin.model.entity.InterfaceInfo;
import com.huanlin.project.common.ErrorCode;
import com.huanlin.project.exception.BusinessException;
import com.huanlin.project.exception.ThrowUtils;
import com.huanlin.project.mapper.InterfaceInfoMapper;
import com.huanlin.project.service.InterfaceInfoService;
import com.hunalin.model.entity.User;
import com.hunalin.model.entity.UserInterfaceInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 */
@Service
@Slf4j
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
    implements InterfaceInfoService {
    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;
    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;
    @Override
    public void validInterfaceInfo(InterfaceInfo InterfaceInfo, boolean add) {
        if (InterfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String name = InterfaceInfo.getName();
        String url = InterfaceInfo.getUrl();
        String method = InterfaceInfo.getMethod();
        // 创建时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(StringUtils.isAnyBlank(name, url, method), ErrorCode.PARAMS_ERROR);
        }
        // 有参数则校验
        if (StringUtils.isNotBlank(name) && name.length() > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口名称过长");
        }
//        if (StringUtils.isNotBlank(content) && content.length() > 8192) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR, "内容过长");
//        }
    }

    @Override
    public InterfaceInfo getInterfaceInfo(String path, String method) {
        if(StringUtils.isAnyBlank(path,method)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<InterfaceInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("url",path);
        wrapper.eq("method",method);
        InterfaceInfo interfaceInfo = this.baseMapper.selectOne(wrapper);
        if(interfaceInfo == null){
            throw  new  BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return interfaceInfo;
    }

    @Override
    public List<Long> selectApiId() {
        QueryWrapper<InterfaceInfo> infoQueryWrapper = new QueryWrapper<>();
        infoQueryWrapper.eq("status",1);
        infoQueryWrapper.eq("isDelete",0);
        ArrayList<Long> ids = new ArrayList<>();
        List<InterfaceInfo> interfaceInfoList = this.baseMapper.selectList(infoQueryWrapper);
        if(interfaceInfoList != null) {
            interfaceInfoList.stream().forEach(item -> {
                Long id = item.getId();
                ids.add(id);
            });
        }
        return ids;
    }

    @Override
    public UserInfoVO getUserInfo(User loginUser) {
        if(loginUser == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"请登录");
        }
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtils.copyProperties(loginUser,userInfoVO);
        List<UserInterface> userInterfaces = new ArrayList<>();
        List<UserInterfaceInfo> info =userInterfaceInfoService.getInterfaceInfoIdByUserId(loginUser.getId());
        if(info != null) {
            info.stream().forEach(item -> {
                InterfaceInfo interfaceInfo = this.baseMapper.selectById(item.getInterfaceInfoId());
                UserInterface userInterface = new UserInterface();
                if(interfaceInfo == null){
                    userInterface.setInterfaceName("此接口不存在");
                }else{
                    userInterface.setInterfaceName(interfaceInfo.getName());
                }
                userInterface.setLeftNum(item.getLeftNum());
                userInterface.setTotalNum(item.getTotalNum());
                userInterfaces.add(userInterface);
            });
        }
        userInfoVO.setInterfaces(userInterfaces);
        return userInfoVO;
    }


}





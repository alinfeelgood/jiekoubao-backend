package com.huanlin.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hunalin.model.entity.UserInterfaceInfo;
import com.huanlin.project.model.vo.UserInterfaceInfoVo;

import java.util.List;

public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {

    List<UserInterfaceInfoVo> getUserInterfaceTotalNums(long limit);

    /**
     * 被调用最多的接口TOP5
     * @param limit
     * @return
     */
    List<UserInterfaceInfo> listTopInvokeInterfaceInfo(int limit);

    /**
     * 最活跃的TOP5用户
     * @param limit
     * @return
     */
    List<UserInterfaceInfo> listTopUserInvokeInterfaceInfo(int limit);

    List<UserInterfaceInfo> selectIdsByUserId(Long id);

}

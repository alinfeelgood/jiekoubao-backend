package com.huanlin.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hunalin.model.entity.UserInterfaceInfo;
import com.huanlin.project.common.ErrorCode;
import com.huanlin.project.exception.BusinessException;
import com.huanlin.project.exception.ThrowUtils;
import com.huanlin.project.mapper.UserInterfaceInfoMapper;
import com.huanlin.project.model.vo.UserInterfaceInfoVo;
import com.huanlin.project.service.UserInterfaceInfoService;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
    implements UserInterfaceInfoService {
    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;
    @Override
    public void validInterfaceInfo(UserInterfaceInfo InterfaceInfo, boolean add) {
        if (InterfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long userId = InterfaceInfo.getUserId();
        Long interfaceInfoId = InterfaceInfo.getInterfaceInfoId();
        Integer leftNum = InterfaceInfo.getLeftNum();
        Integer totalNum = InterfaceInfo.getTotalNum();
        // 创建时，参数不能为空
        if (add) {
//            ThrowUtils.throwIf(StringUtils.isAnyBlank(userId, interfaceInfoId, leftNum,totalNum), ErrorCode.PARAMS_ERROR);
            ThrowUtils.throwIf((userId<=0 || userId==null
                    ||interfaceInfoId <= 0 || interfaceInfoId == null
                    || leftNum < 0 || leftNum == null
                    || totalNum < 0 || totalNum == null),ErrorCode.PARAMS_ERROR);
        }
        // 有参数则校验
        if (leftNum > totalNum) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "剩余次数不能比调用次数多");
        }
    }
    //TODO 高并发情况下 加事务锁

    /**
     *  把 增加调用次数 的 方法 复用 而不是每个开发者开发接口都要写这么一段代码
     *  1、 通用方法 缺点 还是要开发者调用
     *  2、拦截器
     *  3、AOP切面  缺点 每个服务模块独立 都要写一个AOP切面类
     *  4、网关✔
     * @param interfaceinfoId
     * @param userId
     * @return
     */
    @Override
    public boolean addInterfaceCount(long interfaceinfoId, long userId) {
        if(interfaceinfoId <= 0 || userId <= 0){
            throw  new  BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UpdateWrapper<UserInterfaceInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("interfaceinfoId",interfaceinfoId);
        updateWrapper.eq("userId",userId);
        updateWrapper.gt("leftNum",0);
        updateWrapper.setSql("leftNum = leftNum - 1,totalNum = totalNum + 1");
        boolean update = this.update(updateWrapper);
        return update;
    }

    @Override
    public List<UserInterfaceInfoVo> getUserInterfaceTotalNums(long limit) {
        List<UserInterfaceInfoVo> res = this.baseMapper.getUserInterfaceTotalNums(limit);
        return res;
    }

    @Override
    public boolean giveRegistryApiCount(Long userId, List<Long> ids) {
        ids.stream().forEach(id -> {
            UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
            userInterfaceInfo.setUserId(userId);
            userInterfaceInfo.setInterfaceInfoId(id);
            userInterfaceInfo.setTotalNum(0);
            userInterfaceInfo.setLeftNum(20);
            int insert = this.baseMapper.insert(userInterfaceInfo);
            if(insert <= 0){
                log.error("保存注册用户接口调用次数失败");
            }
        });
        return true;
    }

    @Override
    public List<UserInterfaceInfo> getInterfaceInfoIdByUserId(Long id) {
        List<UserInterfaceInfo> info = this.baseMapper.selectIdsByUserId(id);
        return info;
    }
}





package com.hunalin.service;

/**
 *
 */
public interface InnerUserInterfaceInfoService {

    /**
     * 调用接口统计
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    boolean addInterfaceCount(long interfaceInfoId, long userId);

    int getInterfaceLeftCount(long interfaceInfoId,long userId);
}

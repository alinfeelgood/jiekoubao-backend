package com.huanlin.project.model.dto.errorinterface;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建请求
 */
@Data
public class InterfaceErrorAddRequest implements Serializable {

    /**
     * 接口id
     */
    private Long interfaceId;

    /**
     * 接口名称
     */
    private String name;
    /**
     * 反馈人邮箱
     */
    private String email;

    /**
     * 请求参数
     *  json {"name" : "ALin", "type" : "string"}
     */
    private String information;


    /**
     * 异常状态（0-未处理，1-处理完毕）
     */
    private Integer status;

    private static final long serialVersionUID = 1L;

}
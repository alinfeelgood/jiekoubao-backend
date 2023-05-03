package com.huanlin.project.model.vo;

import lombok.Data;

@Data
public class UserInterface {
    /**
     * 总调用次数
     */
    private Integer totalNum;
    /**
     * 剩余调用次数
     */
    private Integer leftNum;
    /**
     * 调用接口名称
     */
    private String interfaceName;
}

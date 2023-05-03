package com.huanlin.project.model.vo;

import lombok.Data;

@Data
public class UserInterfaceInfoVo  {

    /**
     * 接口 id
     */
    private String name;
    /**
     * 总调用次数
     */
    private Integer totalNum;


}
package com.hunalin.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 接口信息
 * @TableName interface_error
 */
@TableName(value ="interface_error")
@Data
public class InterfaceError implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;
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


    /**
     * 创建时间
     */

//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
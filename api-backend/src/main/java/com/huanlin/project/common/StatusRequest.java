package com.huanlin.project.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 删除请求
 */
@Data
public class StatusRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}
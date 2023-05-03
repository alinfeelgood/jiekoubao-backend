package com.huanlin.apiclientsdk.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class WeatherCitys implements Serializable {
    /**
     * 查询条件
     */
    private String params;

    private static final long serialVersionUID = 1L;
}

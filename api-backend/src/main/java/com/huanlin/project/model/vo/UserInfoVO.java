package com.huanlin.project.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class UserInfoVO {
    /**
     * 用户昵称
     */
    private String userName;
    /**
     * 用户头像
     */
    private String userAvatar;

    private List<UserInterface> interfaces;

}

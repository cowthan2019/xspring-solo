package com.danger.app.user.model;

import lombok.Data;

import java.util.Date;

@Data
public class OAuth2BindingModel {
    private Long id;
    private Long userId;
    private String platform;
    private String openId;
    private String name;
    private String headIcon;
    private int gender;
    private Integer status;
    private Integer deleted;
    private Date gmtCreate;
    private Date gmtModified;

}
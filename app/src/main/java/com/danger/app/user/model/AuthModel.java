package com.danger.app.user.model;

import lombok.Data;
import java.util.Date;

@Data
public class AuthModel {
    private Long id;
    private String username;
    private String password;
    private String uid;
    private String sid;
    private String token;
    private Integer roleId;
    private Integer vipLevel;
    private Integer status;
    private Integer userType;
    private String lastLoginDevice;
    private String lastLoginPlatform;
    private Date lastLoginTime;
    private Integer deleted;
    private Date gmtCreate;
    private Date gmtModified;

}
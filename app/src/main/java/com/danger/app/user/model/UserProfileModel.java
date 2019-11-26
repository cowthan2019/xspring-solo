package com.danger.app.user.model;

import lombok.Data;

import java.util.Date;

@Data
public class UserProfileModel {
    private Long id;
    private Long userId;
    private String nickname;
    private String headIcon;
    private String signature;
    private String realname;
    private Integer gender;
    private String email;
    private Integer age;
    private String birth;
    private String mobile;
    private String address;
    private String extra;

    private Integer deleted;
    private Date gmtCreate;
    private Date gmtModified;

}
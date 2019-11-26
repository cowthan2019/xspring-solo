package com.danger.app.user.model;

import lombok.Data;

@Data
public class UserProfileDetailVO {
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

    private String uid;
    private String sid;
    private int role;
}
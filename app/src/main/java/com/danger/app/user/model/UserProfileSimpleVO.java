package com.danger.app.user.model;

import lombok.Data;

@Data
public class UserProfileSimpleVO {
    private Integer gender;
    private Integer age;
    private String nickname;
    private String realname;
    private String signature;
    private String headIcon;

    private String uid;
    private String sid;

}
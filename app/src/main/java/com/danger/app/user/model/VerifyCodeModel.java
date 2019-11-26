package com.danger.app.user.model;

import lombok.Data;

import java.util.Date;


@Data
public class VerifyCodeModel {

    private Long id;
    private String account;
    private String code;

    private Date startTime;
    private Date gmtCreate;
    private Date gmtModified;
}


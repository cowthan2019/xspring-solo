package com.danger.common.model;

import lombok.Data;

import java.util.Date;

@Data
public class AccessLogModel {
    private Long id;
    private Long userId;
    private Long cost;
    private String username;
    private String title;
    private String content;
    private String ip;
    private String location;
    private String module;
    private Long moduleId;
    private String action;
    private Date opTime;
    private String requestType;
    private String requestUrl;
    private String requestParam;
    private Integer deleted;
    private Date gmtCreate;
    private Date gmtModified;

}
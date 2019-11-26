package com.danger.common.model;

import lombok.Data;

import java.util.Date;

@Data
public class ErrorLogModel {
    private Long id;
    private String mainId;
    private Integer platform;
    private Integer logType;
    private Integer status;
    private String ip;
    private String md5;
    private String title;
    private String extra;
    private Integer times;
    private Long firstShowTime;
    private Long lastShowTime;
    private Integer deleted;
    private Date gmtCreate;
    private Date gmtModified;

}
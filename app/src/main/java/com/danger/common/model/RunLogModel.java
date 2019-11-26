package com.danger.common.model;

import lombok.Data;

import java.util.Date;

@Data
public class RunLogModel {
    private Long id;
    private String mainId;
    private String platform;
    private String logType;
    private String status;
    private String ip;
    private String title;
    private String body;
    private Integer deleted;
    private Date gmtCreate;
    private Date gmtModified;

}
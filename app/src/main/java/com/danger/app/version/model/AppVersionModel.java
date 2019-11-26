package com.danger.app.version.model;

import lombok.Data;

@Data
public class AppVersionModel {

    private Long id;
    private String platform;
    private Integer versionCode;
    private String versionName;
    private String changeLog;
    private Integer force;
    private Integer status;
    private String url;

}

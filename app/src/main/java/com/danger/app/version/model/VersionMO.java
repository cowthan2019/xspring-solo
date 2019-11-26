package com.danger.app.version.model;

import lombok.Data;

@Data
public class VersionMO {
    private Long id;
    private String platform;
    private Integer versionCode;
    private String versionName;
    private String changeLog;
    private Integer force;
    private String url;
    private Integer status;

    private int deviceCount;
}
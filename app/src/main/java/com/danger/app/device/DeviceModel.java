package com.danger.app.device;

import lombok.Data;

import java.util.Date;

@Data
public class DeviceModel {
    private Long id;
    private String platform;
    private String deviceId;
    private String brand;
    private String sdk;
    private String extra;
    private Integer appVersion;
    private Integer deleted;
    private Date gmtCreate;
    private Date gmtModified;

}
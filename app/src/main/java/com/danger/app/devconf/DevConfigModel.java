package com.danger.app.devconf;

import lombok.Data;

import java.util.Date;

@Data
public class DevConfigModel {
    private Long id;
    private String name;
    private String val;
    private String summary;
    private Integer status;
    private Integer deleted;
    private Date gmtCreate;
    private Date gmtModified;

}
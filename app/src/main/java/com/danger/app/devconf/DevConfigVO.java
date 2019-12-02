package com.danger.app.devconf;

import lombok.Data;

@Data
public class DevConfigVO {
    private Long id;
    private String name;
    private String val;
    private String summary;
    private Integer status;

}
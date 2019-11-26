package com.danger.common.model;

import lombok.Data;

@Data
public class RunLogForm {
    private Long id;
    private String mainId;
    private String platform;
    private String logType;
    private String status;
    private String ip;
    private String title;
    private String body;

}
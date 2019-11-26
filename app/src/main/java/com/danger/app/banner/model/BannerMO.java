package com.danger.app.banner.model;

import lombok.Data;

@Data
public class BannerMO {
    private Long id;
    private String location;
    private Integer type;
    private String title;
    private String summary;
    private String mediaUrl;
    private String mediaThumb;
    private String mediaType;
    private String redirectUrl;
    private Integer sort;
    private Integer status;

}
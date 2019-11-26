package com.danger.app.banner.model;

import com.danger.common.validator.CanNotBeNull;
import com.danger.common.validator.UrlValue;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Data
public class BannerMorm {

    private Long id;

    @NotNull
    private String location;

    private String title = "";

    private String summary = "";

    @CanNotBeNull
    @UrlValue
    private String mediaUrl;

    @CanNotBeNull
    @UrlValue
    private String redirectUrl;

    @NotNull
    @PositiveOrZero
    private Integer sort;

}
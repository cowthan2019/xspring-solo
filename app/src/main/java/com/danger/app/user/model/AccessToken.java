package com.danger.app.user.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description= "响应数据")
public class AccessToken {

    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "")
    private String accessToken;

    @ApiModelProperty(value = "")
    private String tokenType;

    @ApiModelProperty(value = "")
    private long expiresIn;

    @ApiModelProperty(value = "")
    private int userType;

    @ApiModelProperty(value = "")
    private String uid;

    @ApiModelProperty(value = "")
    private int role;

//    private ThirdPartyTokenVO nim;

}

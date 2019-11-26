package com.danger.app.version.model;

import com.danger.common.validator.CanNotBeNull;
import com.danger.common.validator.EnumValue;
import com.danger.common.validator.UrlValue;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class AppVersionMorm {
    private Long id;

    @CanNotBeNull
    @EnumValue(strValues = {"all", "android", "ios"})
    private String platform;

    @CanNotBeNull
    private Integer versionCode;

    @CanNotBeNull
    @Length(max = 20)
    private String versionName;

    @CanNotBeNull
    @Length(min = 10, max = 300)
    private String changeLog;

    @EnumValue(intValues = {0, 1})
    private Integer force = 0;

    @CanNotBeNull
    @UrlValue
    private String url;

}
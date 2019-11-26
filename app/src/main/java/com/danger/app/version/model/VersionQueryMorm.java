package com.danger.app.version.model;

import com.danger.common.validator.EnumValue;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class VersionQueryMorm {

    @Range(min = 1)
    private Integer page = 1;

    @Range(min = 1, max = 1000)
    private Integer count = 20;

    @EnumValue(strValues = {"all", "android", "ios"})
    private String platform = "all";

    private String keyword = "";

}

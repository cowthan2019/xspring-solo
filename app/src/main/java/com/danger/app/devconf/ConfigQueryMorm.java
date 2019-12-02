package com.danger.app.devconf;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class ConfigQueryMorm {

    @Range(min = 1)
    private Integer page = 1;

    @Range(min = 1, max = 1000)
    private Integer count = 20;

    private String keyword = "";

}

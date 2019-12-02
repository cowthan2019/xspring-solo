package com.danger.app.devconf;

import com.danger.common.validator.CanNotBeNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class DevConfigMorm {
    private Long id;


    @CanNotBeNull
    @Length(min = 2, max = 100)
    private String name;

    @CanNotBeNull
    @Length(min = 0, max = 1024)
    private String val;

    @CanNotBeNull
    @Length(min = 3, max = 300)
    private String summary;

}
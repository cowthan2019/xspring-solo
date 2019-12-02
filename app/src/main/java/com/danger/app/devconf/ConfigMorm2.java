package com.danger.app.devconf;

import com.danger.common.validator.CanNotBeNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Positive;

@Data
public class ConfigMorm2 {

    @CanNotBeNull
    @Positive
    private Long id;

    @CanNotBeNull
    @Length(min = 0, max = 1024)
    private String val;


}
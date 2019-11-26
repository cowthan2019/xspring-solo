package com.danger.app.user.model;

import com.danger.common.validator.CanNotBeNull;
import com.danger.common.validator.EnumValue;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class RegisterForm2 {


    @CanNotBeNull
    @EnumValue(strValues = {"facebook", "google", "twitter", "wx", "qq"})
    private String platform;

    @CanNotBeNull
    @Length(min = 2)
    private String openId;

    @CanNotBeNull
    private String payload;

}
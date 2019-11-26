package com.danger.app.user.model;

import com.danger.common.validator.AccountValue;
import com.danger.common.validator.CanNotBeNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

@Data
public class RegisterForm {


    @CanNotBeNull
    @AccountValue
    private String account;

    @CanNotBeNull
    @Length(min = 6, max = 20, message = "密码不合法")
    private String password;

    @CanNotBeNull
    @Length(min = 3, max = 7, message = "验证码不合法")
    private String code;

    @CanNotBeNull
    @Range(min = 0, max = 1, message = "登录标志位不合法")
    private Integer login = 1;

}

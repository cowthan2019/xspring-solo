package com.danger.app.user.model;

import com.danger.common.validator.AccountValue;
import com.danger.common.validator.CanNotBeNull;
import com.danger.common.validator.PasswordValue;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class PwdResetForm {
    @CanNotBeNull
    @AccountValue
    private String account;


    @CanNotBeNull
    @Length(min = 3, max = 7, message = "验证码不合法")
    private String code;

    @CanNotBeNull
    @PasswordValue
    private String password;

    @CanNotBeNull
    @PasswordValue
    private String passwordConfirm;
}



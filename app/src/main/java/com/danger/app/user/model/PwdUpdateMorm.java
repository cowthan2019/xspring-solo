package com.danger.app.user.model;

import com.danger.common.validator.CanNotBeNull;
import com.danger.common.validator.PasswordValue;
import lombok.Data;

@Data
public class PwdUpdateMorm {
    @CanNotBeNull
    @PasswordValue
    private String oldPassword;

    @CanNotBeNull
    @PasswordValue
    private String newPassword;

    @CanNotBeNull
    @PasswordValue
    private String newPassword2;
}



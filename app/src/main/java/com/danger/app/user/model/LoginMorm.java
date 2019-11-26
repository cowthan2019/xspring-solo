package com.danger.app.user.model;

import com.danger.common.validator.CanNotBeNull;
import com.danger.common.validator.PasswordValue;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class LoginMorm {

    @CanNotBeNull
//    @AccountValue
    @Length(min = 5, max = 40)
    private String account;

    @CanNotBeNull
    @PasswordValue
    private String password;

}

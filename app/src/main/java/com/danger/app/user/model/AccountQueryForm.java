package com.danger.app.user.model;

import com.danger.common.validator.AccountValue;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AccountQueryForm {

    @NotNull(message = "不能为空")
    @AccountValue
    private String account;

//    private int x;
}

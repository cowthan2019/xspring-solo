package com.danger.app.user.model;

import com.danger.common.validator.AccountValue;
import com.danger.common.validator.CanNotBeNull;
import com.danger.common.validator.PasswordValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description= "form")
@Data
public class LoginForm {

    @ApiModelProperty(required = true, value = "form, 账号，邮箱或手机号")
    @CanNotBeNull
    @AccountValue
    private String account;

    @ApiModelProperty(required = true, value = "form, 密码，6到20位")
    @CanNotBeNull
    @PasswordValue
    private String password;

//    @ApiModelProperty(required = true, notes = "xx位")
//    private Integer xx;
//
//    @ApiModelProperty(required = true, notes = "a位")
//    @Valid
//    private A a;
//
//    @ApiModelProperty(required = true, value = "list位")
//    @Valid
//    private List<info.A> list = new ArrayList<>();
//
//    public static class A{
//        @NotBlank(message = "aa 账号不能为空")
//        @EnumValue(strValues = {"11", "22"}, message = "aaa 只能为1122")
//        private String account;
//
//        public String getAccount() {
//            return account;
//        }
//
//        public void setAccount(String account) {
//            this.account = account;
//        }
//    }

}

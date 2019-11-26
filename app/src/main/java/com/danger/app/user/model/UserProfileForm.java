package com.danger.app.user.model;

import com.danger.common.validator.CanNotBeNull;
import com.danger.common.validator.UrlValue;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

@Data
public class UserProfileForm {

    @CanNotBeNull
    @Length(min = 2, max = 64)
    private String nickname;

    @CanNotBeNull
    @UrlValue
    private String headIcon;

    @CanNotBeNull
    @Length(min = 0, max = 256)
    private String signature;

    @CanNotBeNull
    @Length(min = 2, max = 24)
    private String realname;

    @CanNotBeNull
    @Range(min = 0, max = 2)
    private Integer gender;

    @CanNotBeNull
    @Length(min = 0, max = 100)
    private String email;

    @CanNotBeNull
    private Integer age;

    @CanNotBeNull
    private String birth;

    @CanNotBeNull
    private String mobile;

    @CanNotBeNull
    private String address;

    @CanNotBeNull
    private String extra;

}
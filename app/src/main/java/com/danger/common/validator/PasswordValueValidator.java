package com.danger.common.validator;

import com.danger.utils.MyUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValueValidator implements ConstraintValidator<PasswordValue, Object> {


    @Override
    public void initialize(PasswordValue constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context ) {
        String v = value.toString();
        return MyUtils.isValidPassword(v);
    }



}
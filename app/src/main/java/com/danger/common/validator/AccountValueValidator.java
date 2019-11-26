package com.danger.common.validator;

import org.cp4j.core.String2;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AccountValueValidator implements ConstraintValidator<AccountValue, Object> {


    @Override
    public void initialize(AccountValue constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context ) {
        if(value == null) return false;
        String v = value.toString();
        if (!String2.isMobile(v) && !String2.isEmail(v)) {
            return false;
        }
        return true;

    }



}
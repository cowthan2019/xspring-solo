package com.danger.common.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SchemeValueValidator implements ConstraintValidator<SchemeValue, Object> {


    @Override
    public void initialize(SchemeValue constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context ) {
//        return String2.isHttpUrl(value + "");
        return (value + "").startsWith("http");
    }



}
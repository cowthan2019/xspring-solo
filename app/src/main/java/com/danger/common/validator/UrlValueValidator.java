package com.danger.common.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UrlValueValidator implements ConstraintValidator<UrlValue, Object> {


    @Override
    public void initialize(UrlValue constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context ) {
//        return String2.isHttpUrl(value + "");
        return (value + "").startsWith("http");
    }



}
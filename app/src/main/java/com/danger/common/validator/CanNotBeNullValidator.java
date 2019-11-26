package com.danger.common.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CanNotBeNullValidator implements ConstraintValidator<CanNotBeNull, Object> {


    @Override
    public void initialize(CanNotBeNull constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context ) {
        return value != null;
    }



}
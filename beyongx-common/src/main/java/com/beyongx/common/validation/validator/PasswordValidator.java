package com.beyongx.common.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.beyongx.common.utils.ValidateUtils;
import com.beyongx.common.validation.constraints.Password;

public class PasswordValidator implements ConstraintValidator<Password, Object> {

    @Override
    public void initialize(Password password) {
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        return ValidateUtils.isValidPassword(o.toString());
    }
}

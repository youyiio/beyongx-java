package com.beyongx.common.validation.validator;

import com.beyongx.common.utils.ValidateUtils;
import com.beyongx.common.validation.constraints.Mobile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MobileValidator implements ConstraintValidator<Mobile, Object> {

    @Override
    public void initialize(Mobile mobile) {
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        return ValidateUtils.isValidMobile(o.toString());
    }
}

package com.beyongx.common.validation.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.beyongx.common.validation.validator.MobileValidator;

import java.lang.annotation.*;

@Documented
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MobileValidator.class)
public @interface Mobile {

    String message() default "手机号格式不正确";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}


package com.beyongx.common.validation.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.beyongx.common.validation.validator.PasswordValidator;

import java.lang.annotation.*;

@Documented
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
public @interface Password {

    String message() default "密码为6-20位字母、数字或字符(@-_)组合!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

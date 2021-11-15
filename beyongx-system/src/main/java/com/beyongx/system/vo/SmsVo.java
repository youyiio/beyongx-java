package com.beyongx.system.vo;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.beyongx.common.validation.constraints.Mobile;
import com.beyongx.common.validation.group.Always;

import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class SmsVo {
    
    public interface SendCode {

    }
    public interface Login {

    }

    @NotBlank(message = "手机号不能为空", groups = {Always.class})
    @Mobile(message = "手机号格式不正确", groups = {Always.class})
    private String mobile;

    @NotBlank(message = "短信验证码类型不能为空", groups = {SendCode.class})
    private String action;

    @NotNull(message = "验证码不能为空", groups = {Login.class})
    @Digits(message = "验证码格式为6位的数字", integer = 6, fraction = 0, groups = {Login.class})
    @Length(message = "验证码格式为6位的数字", min = 6, max = 6, groups = {Login.class})
    private String code;
}

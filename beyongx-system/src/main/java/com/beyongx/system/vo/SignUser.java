package com.beyongx.system.vo;

import com.beyongx.common.validation.group.Always;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Component
public class SignUser {

    public interface Login {
    }
    public interface Register {
    }

    @NotBlank(message = "用户名不能为空", groups = {Always.class})
    private String username;

    @NotBlank(message = "密码不能为空", groups = {Always.class})
    private String password;

    @NotNull(message = "验证码不能为空", groups = {Register.class})
    @Digits(message = "验证码格式为6位的数字", integer = 6, fraction = 0, groups = {Register.class})
    @Length(message = "验证码格式为6位的数字", min = 6, max = 6, groups = {Register.class})
    private String code;

    private String nickname;
}

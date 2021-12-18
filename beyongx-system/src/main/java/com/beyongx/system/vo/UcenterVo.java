package com.beyongx.system.vo;

import javax.validation.constraints.NotBlank;

import com.beyongx.common.validation.constraints.Password;
import com.beyongx.common.validation.group.Edit;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class UcenterVo {
    
    public static interface ModifyPassword {}

    @NotBlank(message = "昵称不能为空", groups = {Edit.class})
    private String nickname;

    @NotBlank(message = "个人签名不能为空", groups = {Edit.class})
    private String description;

    private String qq;

    private String weixin;

    @Password(message = "密码为6-20位字母、数字或字符(@-_)组合!", groups = {ModifyPassword.class})
    private String password;

    @Password(message = "密码为6-20位字母、数字或字符(@-_)组合!", groups = {ModifyPassword.class})
    private String oldPassword;
}

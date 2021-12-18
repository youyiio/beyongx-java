package com.beyongx.system.vo;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.beyongx.common.validation.constraints.Mobile;
import com.beyongx.common.validation.group.Create;
import com.beyongx.common.validation.group.Edit;
import com.beyongx.system.entity.SysDept;
import com.beyongx.system.entity.SysRole;

import lombok.Data;

@Data
public class UserVo {
    
    public static interface ModifyPassword {

    }

    @NotNull(message = "用户id不能为空", groups= {Edit.class, ModifyPassword.class})
    private Integer id;

    @Mobile(message = "手机号格式不正确!", groups = {Create.class, Edit.class})
    private String mobile;

    @NotBlank(message = "邮箱格式不正确!", groups = {Create.class, Edit.class})
    @Email(message = "邮箱格式不正确!", groups = {Create.class, Edit.class})
    private String email;

    @NotBlank(message = "账户格式不正确!", groups = {Edit.class})
    @Pattern(message = "账户为6-32位字母、数字组合!", regexp="[a-zA-z0-9]{6,32}", groups = {Edit.class})
    private String account;

    @NotBlank(message = "密码为6-20位字母、数字或字符(@-_)组合!", groups = {Create.class})
    @Pattern(message = "密码为6-20位字母、数字或字符(@-_)组合!", regexp="[a-zA-z0-9@_\\-]{6,20}", groups = {Create.class, ModifyPassword.class})
    private String password;

    private Integer status;

    @NotBlank(message = "昵称不能为空!", groups = {Create.class})
    private String nickname;

    private Integer sex;

    private String headUrl;

    private Integer deptId;

    private String qq;

    private String weixin;

    /**
     * 介绍人
     */
    private String referee;

    private Date registerTime;

    private String registerIp;

    private String fromReferee;

    private String entranceUrl;

    private Date lastLoginTime;

    private String lastLoginIp;
    

    private List<Integer> roleIds;

    private SysDept dept;

    private List<SysRole> roles;
}

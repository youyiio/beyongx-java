package com.beyongx.system.vo;

import java.util.Date;
import java.util.List;

import com.beyongx.system.entity.SysDept;
import com.beyongx.system.entity.SysRole;

import lombok.Data;

@Data
public class UserVo {
    
    private Integer id;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 帐号
     */
    private String account;

    /**
     * 密码
     */
    private String password;

    /**
     * 状态:-1.删除;1.申请;2.激活;3.冻结;
     */
    private Integer status;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 性别:1.男;2.女;3.未知;
     */
    private Integer sex;

    /**
     * 头像url
     */
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
    

    private SysDept dept;

    private List<SysRole> roles;
}

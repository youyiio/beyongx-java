package com.beyongx.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户信息表
 * </p>
 *
 * @author youyi.io
 * @since 2021-11-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
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

    /**
     * 盐串
     */
    private String salt;

    private Date registerTime;

    private String registerIp;

    private String fromReferee;

    private String entranceUrl;

    private Date lastLoginTime;

    private String lastLoginIp;


}

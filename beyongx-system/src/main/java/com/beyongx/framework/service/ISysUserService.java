package com.beyongx.framework.service;

import com.beyongx.framework.entity.SysRole;
import com.beyongx.framework.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.beyongx.framework.vo.SignUser;

import java.util.List;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author youyi.io
 * @since 2021-07-01
 */
public interface ISysUserService extends IService<SysUser> {

    SysUser register(SignUser signUser);

    SysUser findByAccount(String account);

    SysUser findByEmail(String email);

    SysUser findByMobile(String mobile);

    boolean verifyPassword(String plainPassword, SysUser user);

    List<SysRole> listRoles(Integer uid);
}

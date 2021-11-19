package com.beyongx.system.service;

import com.beyongx.system.entity.SysJob;
import com.beyongx.system.entity.SysRole;
import com.beyongx.system.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.beyongx.system.vo.SignUser;

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

    SysUser register(SignUser signUser, String ip);

    SysUser login(SignUser signUser, String ip);

    SysUser findByAccount(String account);

    SysUser findByEmail(String email);

    SysUser findByMobile(String mobile);

    boolean verifyPassword(String plainPassword, SysUser user);

    List<SysRole> listRoles(Integer uid);

    List<SysJob> listJobs(Integer uid);
}

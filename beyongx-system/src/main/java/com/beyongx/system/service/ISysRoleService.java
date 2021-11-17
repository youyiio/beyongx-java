package com.beyongx.system.service;

import com.beyongx.system.entity.SysMenu;
import com.beyongx.system.entity.SysRole;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author youyi.io
 * @since 2021-07-01
 */
public interface ISysRoleService extends IService<SysRole> {

    List<SysMenu> listMenu(Integer roleId, String belongsTo);
}

package com.beyongx.system.service;

import com.beyongx.system.entity.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 系统菜单表 服务类
 * </p>
 *
 * @author youyi.io
 * @since 2021-07-01
 */
public interface ISysMenuService extends IService<SysMenu> {

    List<SysMenu> listMenusByRoleId(Integer roleId, String belongsTo);

    List<SysMenu> listMenusByRoleIds(Integer[] roleIds, String belongsTo);

    List<SysMenu> listMenusByRoleIdsAndOther(Integer[] roleIds, String belongsTo, Boolean isMenu);
}

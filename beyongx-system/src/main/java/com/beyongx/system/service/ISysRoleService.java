package com.beyongx.system.service;

import com.beyongx.system.entity.SysMenu;
import com.beyongx.system.entity.SysRole;
import com.beyongx.system.entity.SysUser;
import com.beyongx.system.vo.RoleVo;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
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

    SysRole createRole(RoleVo roleVo);

    SysRole editRole(RoleVo roleVo);

    boolean addMenus(Integer roleId, List<Integer> menus);

    /**
     * 获取角色的菜单列表
     * @param roleId
     * @param belongsTo
     * @return
     */
    List<SysMenu> listMenu(Integer roleId, String belongsTo);

    List<Integer> listMenuId(Integer roleId, String belongsTo);

    IPage<SysUser> listUser(IPage<SysUser> page, Integer roleId);
}

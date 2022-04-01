package com.beyongx.system.service.impl;

import com.beyongx.common.exception.ServiceException;
import com.beyongx.common.vo.Result;
import com.beyongx.system.entity.SysMenu;
import com.beyongx.system.entity.SysRole;
import com.beyongx.system.entity.SysRoleMenu;
import com.beyongx.system.entity.SysUser;
import com.beyongx.system.entity.meta.RoleMeta;
import com.beyongx.system.mapper.SysMenuMapper;
import com.beyongx.system.mapper.SysRoleMapper;
import com.beyongx.system.service.ISysRoleMenuService;
import com.beyongx.system.service.ISysRoleService;
import com.beyongx.system.vo.RoleVo;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author youyi.io
 * @since 2021-07-01
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Autowired
    private SysMenuMapper menuMapper;
    @Autowired
    private ISysRoleMenuService roleMenuService;

    @Override
    public SysRole createRole(RoleVo roleVo) {
        SysRole role = new SysRole();
        try {
            BeanUtils.copyProperties(role, roleVo);
        } catch (Exception e) {            
            log.error("beanutils copyProperites error:", e);
        }

        Date currentTime = new Date();
        role.setCreateTime(currentTime);
        role.setUpdateTime(currentTime);

        baseMapper.insert(role);
        return role;
    }

    @Override
    public SysRole editRole(RoleVo roleVo) {
        SysRole role = baseMapper.selectById(roleVo.getId());
        if (role == null || role.getStatus() == RoleMeta.Status.DELETED.getCode()) {
            throw new ServiceException(Result.Code.E_ROLE_NOT_FOUND, Result.Msg.E_ROLE_NOT_FOUND);
        }
        
        role.setName(roleVo.getName());
        role.setTitle(roleVo.getTitle());
        role.setRemark(roleVo.getRemark());
        role.setUpdateTime(new Date());

        baseMapper.updateById(role);
        return role;
    }

    @Override
    public boolean addMenus(Integer roleId, List<Integer> menuIds) {
        SysRole role = baseMapper.selectById(roleId);
        if (role == null || role.getStatus() == RoleMeta.Status.DELETED.getCode()) {
            throw new ServiceException(Result.Code.E_ROLE_NOT_FOUND, Result.Msg.E_ROLE_NOT_FOUND);
        }

        QueryWrapper<SysRoleMenu> wrapper = new QueryWrapper<>();
        wrapper.eq("role_id", roleId);
        roleMenuService.remove(wrapper);

        List<SysRoleMenu> roleMenuList = menuIds.stream().map(menuId -> {
            SysRoleMenu roleMenu = new SysRoleMenu();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(menuId);
            return roleMenu;
        }).collect(Collectors.toList());

        roleMenuService.saveBatch(roleMenuList);
        return true;
    }

    @Override
    public List<SysMenu> listMenu(Integer roleId, String belongsTo) {
        return menuMapper.selectByRoleId(roleId, belongsTo);
    }

    @Override
    public List<Integer> listMenuId(Integer roleId, String belongsTo) {
        List<SysMenu> menus = menuMapper.selectByRoleId(roleId, belongsTo);
        List<Integer> menuIds = menus.stream().map(menu -> menu.getId()).collect(Collectors.toList());
        return menuIds;
    }

    @Override
    public IPage<SysUser> listUser(IPage<SysUser> page, Integer roleId) {
        return baseMapper.selectUsersByIdPage(page, roleId);
    }
}

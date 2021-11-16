package com.beyongx.system.service.impl;

import com.beyongx.system.entity.SysMenu;
import com.beyongx.system.entity.SysRole;
import com.beyongx.system.mapper.SysMenuMapper;
import com.beyongx.system.mapper.SysRoleMapper;
import com.beyongx.system.service.ISysMenuService;
import com.beyongx.system.service.ISysRoleService;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

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

    @Override
    public List<SysMenu> listMenu(Integer roleId) {
        return menuMapper.selectByRoleId(roleId);
    }
}

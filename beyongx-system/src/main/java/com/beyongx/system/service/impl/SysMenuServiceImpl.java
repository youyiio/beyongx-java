package com.beyongx.system.service.impl;

import com.beyongx.system.entity.SysMenu;
import com.beyongx.system.mapper.SysMenuMapper;
import com.beyongx.system.service.ISysMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 系统菜单表 服务实现类
 * </p>
 *
 * @author youyi.io
 * @since 2021-07-01
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    @Override
    public List<SysMenu> listMenusByRoleId(Integer roleId) {
        return baseMapper.selectByRoleId(roleId);
    }

    @Override
    public List<SysMenu> listMenusByRoleIds(Integer[] roleIds) {
        return baseMapper.selectByRoleIds(roleIds);
    }

    @Override
    public List<SysMenu> listMenusByRoleIdsAndOther(Integer[] roleIds, Boolean isMenu) {
        return baseMapper.selectByRoleIdsAndOther(roleIds, isMenu);
    }
}

package com.beyongx.framework.service.impl;

import com.beyongx.framework.entity.SysMenu;
import com.beyongx.framework.mapper.SysMenuMapper;
import com.beyongx.framework.service.ISysMenuService;
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
    public List<SysMenu> listMenus(Integer roleId) {
        return baseMapper.selectByRoleId(roleId);
    }
}

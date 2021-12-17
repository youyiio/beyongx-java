package com.beyongx.system.service.impl;

import com.beyongx.common.exception.ServiceException;
import com.beyongx.common.vo.Result;
import com.beyongx.framework.utils.BeanPropertyUtils;
import com.beyongx.system.entity.SysMenu;
import com.beyongx.system.entity.meta.MenuMeta;
import com.beyongx.system.mapper.SysMenuMapper;
import com.beyongx.system.service.ISysMenuService;
import com.beyongx.system.vo.MenuVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
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
    public SysMenu createMenu(MenuVo menuVo) {
        SysMenu menu = new SysMenu();
        try {
            BeanUtils.copyProperties(menuVo, menu);;
        } catch (Exception e) {            
            log.error("beanutils copyProperites error:", e);
        }

        if (menu.getPid() == null) {
            menu.setPid(0);
        }
        if (menu.getStatus() == null) {
            menu.setStatus(MenuMeta.Status.ACTIVE.getCode());
        }
        if (menu.getSort() == null) {
            menu.setSort(0);
        }

        Date currentTime = new Date();
        menu.setUpdateTime(currentTime);
        menu.setCreateTime(currentTime);

        baseMapper.insert(menu);

        return menu;
    }

    @Override
    public SysMenu editMenu(MenuVo menuVo) {
        SysMenu menu = baseMapper.selectById((Integer)menuVo.getId());
        if (menu == null) {
            throw new ServiceException(Result.Code.E_MENU_NOT_FOUND, Result.Msg.E_MENU_NOT_FOUND);
        }

        try {
            String[] ignoreProperties = BeanPropertyUtils.getNullPropertyNames(menuVo);
            BeanUtils.copyProperties(menuVo, menu, ignoreProperties);
        } catch (Exception e) {            
            log.error("beanutils copyProperites error:", e);
        }

        Date currentTime = new Date();
        menu.setUpdateTime(currentTime);

        baseMapper.updateById(menu);

        return menu;
    }

    @Override
    public List<SysMenu> listMenusByRoleId(Integer roleId, String belongsTo) {
        return baseMapper.selectByRoleId(roleId, belongsTo);
    }

    @Override
    public List<SysMenu> listMenusByRoleIds(Integer[] roleIds, String belongsTo) {
        return baseMapper.selectByRoleIds(roleIds, belongsTo);
    }

    @Override
    public List<SysMenu> listMenusByRoleIdsAndOther(Integer[] roleIds, String belongsTo, Boolean isMenu) {
        return baseMapper.selectByRoleIdsAndOther(roleIds, belongsTo, isMenu);
    }
}

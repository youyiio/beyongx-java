package com.beyongx.framework.mapper;

import com.beyongx.framework.entity.SysMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 系统菜单表 Mapper 接口
 * </p>
 *
 * @author youyi.io
 * @since 2021-07-01
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    List<SysMenu> selectByRoleId(Integer roleId);

}

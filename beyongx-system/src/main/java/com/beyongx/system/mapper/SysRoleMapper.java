package com.beyongx.system.mapper;

import com.beyongx.system.entity.SysRole;
import com.beyongx.system.entity.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author youyi.io
 * @since 2021-07-01
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {

    IPage<SysUser> selectUsersByIdPage(IPage<SysUser> page, Integer id);
}

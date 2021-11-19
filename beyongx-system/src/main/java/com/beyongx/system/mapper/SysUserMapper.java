package com.beyongx.system.mapper;

import com.beyongx.system.entity.SysJob;
import com.beyongx.system.entity.SysRole;
import com.beyongx.system.entity.SysUser;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 用户信息表 Mapper 接口
 * </p>
 *
 * @author youyi.io
 * @since 2021-07-01
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

    /** 关联表定义 start */
    
    List<SysRole> selectRolesById(Integer id);

    List<SysJob> selectJobsById(Integer id);

    /** 关联表定义 end */

}

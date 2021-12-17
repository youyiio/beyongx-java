package com.beyongx.system.service;

import com.beyongx.system.entity.SysDept;
import com.beyongx.system.vo.DeptVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 部门表 服务类
 * </p>
 *
 * @author youyi.io
 * @since 2021-07-01
 */
public interface ISysDeptService extends IService<SysDept> {

    SysDept createDept(DeptVo deptVo);

    SysDept editDept(DeptVo deptVo);
}

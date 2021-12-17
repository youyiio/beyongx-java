package com.beyongx.system.service.impl;

import com.beyongx.common.exception.ServiceException;
import com.beyongx.common.vo.Result;
import com.beyongx.framework.utils.BeanPropertyUtils;
import com.beyongx.system.entity.SysDept;
import com.beyongx.system.entity.meta.DeptMeta;
import com.beyongx.system.mapper.SysDeptMapper;
import com.beyongx.system.service.ISysDeptService;
import com.beyongx.system.vo.DeptVo;

import java.util.Date;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 部门表 服务实现类
 * </p>
 *
 * @author youyi.io
 * @since 2021-07-01
 */
@Service
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements ISysDeptService {

    @Override
    public SysDept createDept(DeptVo deptVo) {
        SysDept dept = new SysDept();
        try {
            BeanUtils.copyProperties(deptVo, dept);;
        } catch (Exception e) {            
            log.error("beanutils copyProperites error:", e);
        }

        if (dept.getPid() == null) {
            dept.setPid(0);
        }
        if (dept.getStatus() == null) {
            dept.setStatus(DeptMeta.Status.ONLINE.getCode());
        }
        if (dept.getSort() == null) {
            dept.setSort(0);
        }

        Date currentTime = new Date();
        dept.setUpdateTime(currentTime);
        dept.setCreateTime(currentTime);

        baseMapper.insert(dept);

        return dept;
    }

    @Override
    public SysDept editDept(DeptVo deptVo) {
        SysDept dept = baseMapper.selectById((Integer)deptVo.getId());
        if (dept == null) {
            throw new ServiceException(Result.Code.E_DEPT_NOT_FOUND, Result.Msg.E_DEPT_NOT_FOUND);
        }

        try {
            String[] ignoreProperties = BeanPropertyUtils.getNullPropertyNames(deptVo);
            BeanUtils.copyProperties(deptVo, dept, ignoreProperties);
        } catch (Exception e) {            
            log.error("beanutils copyProperites error:", e);
        }

        Date currentTime = new Date();
        dept.setUpdateTime(currentTime);

        baseMapper.updateById(dept);

        return dept;
    }

}

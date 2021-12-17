package com.beyongx.system.controller;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.beyongx.common.utils.TreeUtils;
import com.beyongx.common.validation.group.Always;
import com.beyongx.common.validation.group.Create;
import com.beyongx.common.validation.group.Edit;
import com.beyongx.common.vo.Result;
import com.beyongx.framework.utils.PageUtils;
import com.beyongx.framework.vo.PageVo;
import com.beyongx.system.entity.SysDept;
import com.beyongx.system.entity.meta.DeptMeta;
import com.beyongx.system.service.ISysDeptService;
import com.beyongx.system.vo.DeptVo;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 部门表 前端控制器
 * </p>
 *
 * @author youyi.io
 * @since 2021-07-01
 */
@RestController
@RequestMapping("/api/dept")
@Slf4j
public class DeptController {

    @Autowired
    private ISysDeptService deptService;

    @RequiresPermissions("dept:list")
    @RequestMapping(value="/list", method = {RequestMethod.GET, RequestMethod.POST})
    public Result list(@Validated @RequestBody PageVo pageVo) {
        QueryWrapper<SysDept> queryWrapper = new QueryWrapper<>();
        
        //条件过滤
        Map<String, Object> filters = new HashMap<>();
        filters.putAll(pageVo.getFilters());
        
        if (filters.containsKey("status")) {
            queryWrapper.eq("status", (Integer)filters.remove("status"));
        } else {
            queryWrapper.ne("status", DeptMeta.Status.DELETED.getCode());
        }        

        if (filters.containsKey("keyword")) {
            String keyword = (String)filters.remove("keyword");
            queryWrapper.like("title", keyword);
        }

        Integer pid = 0;
        if (filters.containsKey("pid")) {
            pid = (Integer)filters.remove("pid");
        }
        String struct = "tree";
        if (filters.containsKey("struct")) {
            struct = (String)filters.remove("struct");
        }
        Integer depth = 0; // 0表示没有限制
        if (filters.containsKey("depth")) {
            depth = (Integer)filters.remove("depth");
        }

        //排序
        Map<String, String> orders = pageVo.getOrders();
        if (orders.size() == 0) {
            queryWrapper.orderByAsc("sort").orderByAsc("id");
        } else {
            for (String key : orders.keySet()) {
                String val = orders.get(key);
                Boolean isAsc = val.equalsIgnoreCase("asc");
                queryWrapper.orderBy(true, isAsc, key);
            }
        }

        List<SysDept> deptList = deptService.list(queryWrapper);
        if (CollectionUtils.isEmpty(deptList)) {
            IPage<DeptVo> pageList = new Page<>(pageVo.getPage(), pageVo.getSize());
            return Result.success(pageList);
        }

        //封装及分页
        List<DeptVo> deptVoList = deptList.stream().map(dept -> {
            DeptVo deptVo = new DeptVo();
            try {
                BeanUtils.copyProperties(deptVo, dept);
            } catch(Exception e) {
                log.error("bean copy error", e);
            }
            
            return deptVo;
        }).collect(Collectors.toList());

        List<DeptVo> treeMenuVoList = TreeUtils.parse(pid, deptVoList);

        IPage<DeptVo> pageList = PageUtils.getPages(pageVo.getPage(), pageVo.getSize(), treeMenuVoList);

        return Result.success(pageList);
    }

    @RequiresPermissions("dept:create")
    @PostMapping("/create")
    public Result create(@Validated({Always.class, Create.class}) @RequestBody DeptVo deptVo) {
        SysDept dept = deptService.createDept(deptVo);

        return Result.success(dept);
    }

    @RequiresPermissions("dept:edit")
    @PostMapping("/edit")
    public Result edit(@Validated({Always.class, Edit.class}) @RequestBody DeptVo deptVo) {
        SysDept dept = deptService.editDept(deptVo);
        
        return Result.success(dept);
    }

    @RequiresPermissions("dept:delete")
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable(value="id") Integer id) {
        SysDept dept = deptService.getById(id);
        if (dept == null) {
            return Result.error(Result.Code.E_DEPT_NOT_FOUND, "部门不存在!");
        }

        dept.setStatus(DeptMeta.Status.DELETED.getCode());
        dept.setUpdateTime(new Date());

        deptService.updateById(dept);

        return Result.success(null);
    }
}

package com.beyongx.system.controller;



import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.beyongx.common.validation.group.Always;
import com.beyongx.common.validation.group.Create;
import com.beyongx.common.validation.group.Edit;
import com.beyongx.common.vo.Result;
import com.beyongx.framework.vo.PageVo;
import com.beyongx.system.entity.SysJob;
import com.beyongx.system.entity.meta.JobMeta;
import com.beyongx.system.service.ISysJobService;
import com.beyongx.system.vo.JobVo;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author youyi.io
 * @since 2021-07-01
 */
@RestController
@RequestMapping("/api/job")
@Slf4j
public class JobController {

    @Autowired
    private ISysJobService jobService;

    @RequiresPermissions("job:list")
    @RequestMapping(value="/list", method = {RequestMethod.GET, RequestMethod.POST})
    public Result list(@Validated @RequestBody PageVo pageVo) {
        QueryWrapper<SysJob> queryWrapper = new QueryWrapper<>();
        
        //条件过滤
        Map<String, Object> filters = new HashMap<>();
        filters.putAll(pageVo.getFilters());
        
        if (filters.containsKey("status")) {
            queryWrapper.eq("status", (Integer)filters.remove("status"));
        } else {
            queryWrapper.ne("status", JobMeta.Status.DELETED.getCode());
        }        

        if (filters.containsKey("title")) {
            String keyword = (String)filters.remove("title");
            queryWrapper.like("title", keyword);
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

        IPage<SysJob> page = new Page<>(pageVo.getPage(), pageVo.getSize());
        
        IPage<SysJob> pageList = jobService.page(page, queryWrapper);
        if (CollectionUtils.isEmpty(pageList.getRecords())) {
            return Result.success(pageList);
        }

        return Result.success(pageList);
    }

    @RequiresPermissions("job:create")
    @PostMapping("/create")
    public Result create(@Validated({Always.class, Create.class}) @RequestBody JobVo jobVo) {
        SysJob job = jobService.createJob(jobVo);

        return Result.success(job);
    }

    @RequiresPermissions("job:edit")
    @PostMapping("/edit")
    public Result edit(@Validated({Always.class, Edit.class}) @RequestBody JobVo jobVo) {
        SysJob job = jobService.editJob(jobVo);

        return Result.success(job);
    }

    @RequiresPermissions("job:delete")
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable(value="id") Integer id) {
        SysJob dept = jobService.getById(id);
        if (dept == null) {
            return Result.error(Result.Code.E_JOB_NOT_FOUND, "岗位不存在!");
        }

        dept.setStatus(JobMeta.Status.DELETED.getCode());
        dept.setUpdateTime(new Date());

        jobService.updateById(dept);

        return Result.success(null);
    }

}

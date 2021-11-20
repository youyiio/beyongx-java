package com.beyongx.system.controller;



import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.beyongx.common.vo.Result;
import com.beyongx.framework.vo.PageVo;
import com.beyongx.system.entity.SysJob;
import com.beyongx.system.service.ISysJobService;

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
        
        //排序
        Map<String, String> orders = pageVo.getOrders();
        if (orders.size() == 0) {
            queryWrapper.orderByAsc("id");
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

    @RequiresPermissions("job:query")
    @GetMapping("/{id}")
    public Result query(@PathVariable(value="id") Integer id) {
        return Result.success(null);
    }

    @RequiresPermissions("job:create")
    @PostMapping("/create")
    public Result create() {
        return Result.success(null);
    }

    @RequiresPermissions("job:edit")
    @PostMapping("/edit")
    public Result edit() {
        return Result.success(null);
    }

    @RequiresPermissions("job:delete")
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable(value="id") Integer id) {
        return Result.success(null);
    }

}

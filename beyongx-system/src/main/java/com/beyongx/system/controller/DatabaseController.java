package com.beyongx.system.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.beyongx.common.vo.Result;
import com.beyongx.framework.entity.Table;
import com.beyongx.framework.service.IDbService;
import com.beyongx.framework.vo.PageVo;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/db")
@Slf4j
public class DatabaseController {
    
    @Autowired
    private IDbService dbService;

    @RequiresPermissions("database:tables")
    @RequestMapping(value="/tables", method = {RequestMethod.GET, RequestMethod.POST})
    public Result listTables(@Validated @RequestBody PageVo pageVo) {
        QueryWrapper<Table> queryWrapper = new QueryWrapper<>();
        
        //条件过滤
        Map<String, Object> filters = new HashMap<>();
        filters.putAll(pageVo.getFilters());
        String database = (String)filters.get("database");

        IPage<Table> page = new Page<>(pageVo.getPage(), pageVo.getSize());
        
        IPage<Table> pageList = dbService.listTables(page, database);
        if (CollectionUtils.isEmpty(pageList.getRecords())) {
            return Result.success(pageList);
        }

        return Result.success(pageList);
    }

    @RequiresPermissions("database:databases")
    @RequestMapping(value="/databases", method = {RequestMethod.GET, RequestMethod.POST})
    public Result listDatabases(@Validated @RequestBody PageVo pageVo) {
        // QueryWrapper<Table> queryWrapper = new QueryWrapper<>();
        
        // IPage<Table> page = new Page<>(pageVo.getPage(), pageVo.getSize());
        
        List<Map<String, Object>> list = dbService.listDatabases();

        return Result.success(list);
    }

}

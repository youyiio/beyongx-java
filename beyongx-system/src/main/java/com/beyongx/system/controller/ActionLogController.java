package com.beyongx.system.controller;

import java.util.HashMap;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.beyongx.common.vo.Result;
import com.beyongx.framework.vo.PageVo;
import com.beyongx.system.entity.SysActionLog;
import com.beyongx.system.service.ISysActionLogService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/log")
@Slf4j
public class ActionLogController {
    
    @Autowired
    private ISysActionLogService actionLogService;

    @RequiresPermissions("log:list")
    @RequestMapping(value="/list", method = {RequestMethod.GET, RequestMethod.POST})
    public Result list(@Validated @RequestBody PageVo pageVo) {
        QueryWrapper<SysActionLog> queryWrapper = new QueryWrapper<>();
        
        //条件过滤
        Map<String, Object> filters = new HashMap<>();
        filters.putAll(pageVo.getFilters());
        
        String keyword = (String)filters.remove("keyword");
        if (StringUtils.isNotBlank(keyword)) {            
            queryWrapper.like("user_agent", keyword);
        }
        String startTime = (String)filters.remove("startTime");
        if (StringUtils.isNotBlank(startTime)) {
            queryWrapper.ge("action_time", startTime);
        }
        String endTime = (String)filters.remove("endTime");
        if (StringUtils.isNotBlank(endTime)) {            
            queryWrapper.lt("action_time", endTime);
        }
        //其他条件
        for (String key : filters.keySet()) {
            queryWrapper.eq(key, filters.get(key));
        }
        

        //排序
        Map<String, String> orders = pageVo.getOrders();
        if (orders.size() == 0) {
            queryWrapper.orderByDesc("id");
        } else {
            for (String key : orders.keySet()) {
                String val = orders.get(key);
                Boolean isAsc = val.equalsIgnoreCase("asc");
                queryWrapper.orderBy(true, isAsc, key);
            }            
        }
        
        IPage<SysActionLog> page = new Page<>(pageVo.getPage(), pageVo.getSize());
        
        IPage<SysActionLog> pageList = actionLogService.page(page, queryWrapper);
        if (CollectionUtils.isEmpty(pageList.getRecords())) {
            return Result.success(pageList);
        }

        return Result.success(pageList);
    }
}

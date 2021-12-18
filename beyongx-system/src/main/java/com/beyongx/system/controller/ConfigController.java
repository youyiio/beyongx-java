package com.beyongx.system.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.beyongx.common.validation.group.Always;
import com.beyongx.common.validation.group.Create;
import com.beyongx.common.vo.Result;
import com.beyongx.framework.vo.PageVo;
import com.beyongx.system.entity.SysConfig;
import com.beyongx.system.entity.meta.ConfigMeta;
import com.beyongx.system.service.ISysConfigService;
import com.beyongx.system.vo.ConfigVo;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
 * 系统字典表 前端控制器
 * </p>
 *
 * @author youyi.io
 * @since 2021-07-01
 */
@RestController
@RequestMapping("/api/config")
@Slf4j
public class ConfigController {

    @Autowired
    private ISysConfigService configService;

    @RequiresPermissions("config:list")
    @RequestMapping(value="/list", method = {RequestMethod.GET, RequestMethod.POST})
    public Result list(@Validated @RequestBody PageVo pageVo) {
        QueryWrapper<SysConfig> queryWrapper = new QueryWrapper<>();
        
        //条件过滤
        Map<String, Object> filters = new HashMap<>();
        filters.putAll(pageVo.getFilters());

        if (filters.containsKey("keyword")) {
            String keyword = (String)filters.remove("keyword");
            queryWrapper.like("name", keyword);
        }
        if (filters.containsKey("group")) {
            String group = (String)filters.remove("group");
            queryWrapper.like("`group`", group);
        }
        if (filters.containsKey("key")) {
            String key = (String)filters.remove("key");
            queryWrapper.like("`key`", key);
        }
        
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

        IPage<SysConfig> page = new Page<>(pageVo.getPage(), pageVo.getSize());
        
        IPage<SysConfig> pageList = configService.page(page, queryWrapper);
        if (CollectionUtils.isEmpty(pageList.getRecords())) {
            return Result.success(pageList);
        }

        return Result.success(pageList);
    }

    @RequiresPermissions("config:groups")
    @RequestMapping(value="/groups", method = {RequestMethod.GET, RequestMethod.POST})
    public Result groups(@Validated @RequestBody PageVo pageVo) {
        QueryWrapper<SysConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", ConfigMeta.Status.ONLINE.getCode());
        queryWrapper.select("distinct `group`");

        IPage<Map<String, Object>> page = new Page<>(pageVo.getPage(), pageVo.getSize());
        IPage<Map<String, Object>> pageList = configService.pageMaps(page, queryWrapper);

        return Result.success(pageList);
    }

    @RequiresPermissions("config:create")
    @PostMapping("/create")
    public Result create(@Validated({Always.class, Create.class}) @RequestBody ConfigVo configVo) {
        SysConfig config = configService.createConfig(configVo);

        return Result.success(config);
    }

    @RequiresPermissions("config:edit")
    @PostMapping("/edit")
    public Result edit(@Validated({Always.class, Create.class}) @RequestBody ConfigVo configVo) {
        SysConfig config = configService.editConfig(configVo);

        return Result.success(config);
    }

    @RequiresPermissions("config:delete")
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable(value="id") Integer id) {
        SysConfig config = configService.getById(id);
        if (config == null) {
            return Result.error(Result.Code.E_CONFIG_NOT_FOUND, "字典不存在!");
        }

        configService.removeById(id);

        return Result.success(null);
    }


    //查询字典信息
    @RequiresPermissions("config:query")
    @PostMapping("/query")
    public Result queryByKey(@RequestBody ConfigVo configVo) {
        SysConfig sysConfig = null;
        if (StringUtils.isBlank(configVo.getGroup())) {
            sysConfig = configService.getByKey(configVo.getKey());
        } else {
            sysConfig = configService.getByGroupAndKey(configVo.getGroup(), configVo.getKey());
        }

        if (sysConfig == null) {
            return Result.error(Result.Code.E_DATA_NOT_FOUND, "字典未找到!");
        }

        return Result.success(sysConfig);
    }

    //查询状态字典
    @RequiresPermissions("config:status")
    @GetMapping("/{name}/status")
    public Result statusDicts(@PathVariable(value="name") String name) {
        String group = name + "_status";

        QueryWrapper<SysConfig> wrapper = new QueryWrapper<>();
        wrapper.eq("`group`", group);
        wrapper.eq("status", ConfigMeta.Status.ONLINE.getCode());
        
        List<SysConfig> list = configService.list(wrapper);
        if (CollectionUtils.isEmpty(list)) {
            return Result.success(list);
        }

        List<Map<String, Object>> statusMaps = list.stream().map(config -> {
            Map<String, Object> map = new HashMap<>();
            map.put(config.getKey(), config.getValue());
            return map;
        }).collect(Collectors.toList());

        return Result.success(statusMaps);
    }
}

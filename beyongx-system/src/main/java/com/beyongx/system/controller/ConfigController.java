package com.beyongx.system.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.beyongx.common.vo.Result;
import com.beyongx.system.entity.SysConfig;
import com.beyongx.system.service.ISysConfigService;
import com.beyongx.system.vo.ConfigVo;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Result list() {
        return Result.success(null);
    }

    @RequiresPermissions("config:query")
    @GetMapping("/{id}")
    public Result query(@PathVariable(value="id") Integer id) {
        return Result.success(null);
    }

    @RequiresPermissions("config:list")
    @PostMapping("/create")
    public Result create() {
        return Result.success(null);
    }

    @RequiresPermissions("config:edit")
    @PostMapping("/edit")
    public Result edit() {
        return Result.success(null);
    }

    @RequiresPermissions("config:list")
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable(value="id") Integer id) {
        return Result.success(null);
    }


    @RequiresPermissions("config:query")
    @DeleteMapping("/query")
    public Result queryByKey(@RequestBody ConfigVo configVo) {
        SysConfig sysConfig = null;
        if (StringUtils.isBlank(configVo.getGroup())) {
            sysConfig = configService.getByKey(configVo.getKey());
        } else {
            sysConfig = configService.getByGroupAndKey(configVo.getGroup(), configVo.getKey());
        }

        return Result.success(sysConfig);
    }
}

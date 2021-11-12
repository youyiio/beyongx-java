package com.beyongx.system.controller;


import com.beyongx.common.vo.Result;
import com.beyongx.system.entity.SysUser;
import com.beyongx.system.service.ISysUserService;
import com.beyongx.framework.shiro.JwtUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 用户中心接口类 前端控制器
 * </p>
 *
 * @author youyi.io
 * @since 2021-07-01
 */
@RestController
@RequestMapping("/api/ucenter")
@Slf4j
public class UcenterController {

    @Autowired
    private ISysUserService userService;

    @RequiresPermissions("ucenter:getInfo")
    @GetMapping("/getInfo")
    public Result getInfo() {
        JwtUser jwtUser = (JwtUser) SecurityUtils.getSubject().getPrincipal();

        SysUser user = userService.getById(jwtUser.getUid());

        Map<String, Object> data = new HashMap<>();
        
        data.put("nickname", user.getNickname());
        data.put("headUrl", user.getHeadUrl());
        data.put("description", "");
        //data.put("roles", user.getRoleList());
        data.put("roles", new String[]{"admin", "test"});

        return Result.success(data);
    }
}

package com.beyongx.framework.controller;


import com.beyongx.common.vo.Result;
import com.beyongx.framework.shiro.JwtUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
@RequestMapping("/api/user")
@Slf4j
public class UcenterController {

    //@RequiresPermissions("user:getInfo")
    @GetMapping("/getInfo")
    public Result getInfo() {
        JwtUser jwtUser = (JwtUser) SecurityUtils.getSubject().getPrincipal();

        Map<String, Object> data = new HashMap<>();
        //data.put("roles", user.getRoleList());
        data.put("roles", new String[]{"admin", "tester"});
        data.put("nickname", jwtUser.getUsername());
        data.put("avatar", "https://www.ituizhan.com/static/cw/assets/images/index/f04.jpg");
        data.put("introduction", "description");

        return Result.success(data);
    }
}

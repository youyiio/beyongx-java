package com.beyongx.framework.controller;

import com.beyongx.common.utils.DateTimeUtils;
import com.beyongx.common.utils.ValidateUtils;
import com.beyongx.common.validation.group.Always;
import com.beyongx.common.vo.Result;
import com.beyongx.framework.entity.SysUser;
import com.beyongx.framework.entity.meta.UserMeta;
import com.beyongx.framework.shiro.JwtUser;
import com.beyongx.framework.shiro.JwtUtils;
import com.beyongx.framework.service.ISysUserService;
import com.beyongx.framework.vo.SignUser;
import lombok.extern.slf4j.Slf4j;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/sign")
@Slf4j
public class SignController {

    @Autowired
    private ISysUserService userService;

    @PostMapping(value = "/login")
    public Result login(@Validated({Always.class,SignUser.Login.class}) @RequestBody SignUser signUser) {

        String username = signUser.getUsername();
        String plainPassword = signUser.getPassword(); //明文密码

        SysUser user = null;
        if (ValidateUtils.isValidEmail(username)) {
            user = userService.findByEmail(signUser.getUsername());
        } else if (ValidateUtils.isValidMobile(username)) {
            user = userService.findByMobile(signUser.getUsername());
        } else {
            user = userService.findByAccount(signUser.getUsername());
        }
        
        if (user == null) {
            return Result.error(Result.Code.E_PARAM_ERROR, "用户不存在!");
        }
        if (!userService.verifyPassword(plainPassword, user)) {
            return Result.error(Result.Code.E_PARAM_ERROR, "用户账号或密码不正确!");
        }

        if (user.getStatus() == UserMeta.Status.DELETED.getCode()) {
            return Result.error(Result.Code.E_PARAM_ERROR, "用户不存在!");
        }
        if (user.getStatus() == UserMeta.Status.FREED.getCode()) {
            return Result.error(Result.Code.E_PARAM_ERROR, "用户已冻结!");
        }

        JwtUser jwtUser = new JwtUser();
        jwtUser.setUid(user.getId());
        jwtUser.setUsername(user.getAccount());
        jwtUser.setSalt(DateTimeUtils.getLongFormat(user.getRegisterTime()));

        //通过认证, 生成签名
        String token = JwtUtils.sign(jwtUser);
        Map<String, Object> data = new HashMap<>();
        data.put("uid", user.getId());
        data.put("nickname", username);
        data.put("token", token);
        data.put("expired", JwtUtils.EXPIRE_TIME); //过期秒数

        return Result.success(data);
    }

    @PostMapping("/register")
    public Result register(@Validated({Always.class,SignUser.Register.class}) @RequestBody SignUser signUser) {

        SysUser user = userService.register(signUser);

        Map<String, Object> data = new HashMap<>();
        data.put("uid", user.getId());
        data.put("username", signUser.getUsername());

        return Result.success(data);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public Result logout() {
        // 清除缓存数据

        return Result.success(null);
    }

    @RequiresPermissions("order")
    @RequestMapping(value = "/order", method = RequestMethod.POST)
    public Result order(@RequestBody Map<String, Object> map) {

        return Result.success(map);
    }

}

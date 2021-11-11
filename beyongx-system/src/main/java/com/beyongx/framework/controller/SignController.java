package com.beyongx.framework.controller;

import com.beyongx.common.utils.DateTimeUtils;
import com.beyongx.common.utils.IpUtils;
import com.beyongx.common.utils.ValidateUtils;
import com.beyongx.common.validation.group.Always;
import com.beyongx.common.vo.Result;
import com.beyongx.framework.entity.SysUser;
import com.beyongx.framework.entity.meta.UserMeta;
import com.beyongx.framework.shiro.JwtUser;
import com.beyongx.framework.shiro.JwtUtils;
import com.beyongx.framework.service.ISysActionLogService;
import com.beyongx.framework.service.ISysUserService;
import com.beyongx.framework.vo.SignUser;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/api/sign")
@Slf4j
public class SignController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ISysUserService userService;
    @Autowired
    private ISysActionLogService actionLogService;
    
    @PostMapping(value = "/login")
    public Result login(@Validated({Always.class,SignUser.Login.class}) @RequestBody SignUser signUser, HttpServletRequest request) {

        String username = signUser.getUsername();
        String plainPassword = signUser.getPassword(); //明文密码

        //登录次数判断
        String tryLoginCountMark = username + "_try_login_count";
        Object tryLoginCountVal = redisTemplate.opsForValue().get(tryLoginCountMark);
        Integer tryLoginCount = tryLoginCountVal == null ? 0 : (Integer)tryLoginCountVal;
        if (tryLoginCount >= 3 && tryLoginCount < 5 && StringUtils.isBlank(signUser.getCode())) {
            return Result.error(Result.Code.E_RISK_ASSE_CODE_CHECK, Result.Msg.E_RISK_ASSE_CODE_CHECK);
        }
        if (tryLoginCount > 5) {
            return Result.error(Result.Code.E_USER_STATE_FREED, Result.Msg.E_USER_STATE_FREED);
        }
        if (tryLoginCount >= 5) {
            redisTemplate.opsForValue().set(tryLoginCountMark, tryLoginCount + 1);
            return Result.error(Result.Code.E_USER_STATE_FREED, Result.Msg.E_USER_STATE_FREED);
        }

        SysUser user = null;
        try {
            String ip = IpUtils.getIpAddr(request);
            user = userService.login(signUser, ip);
            if (user == null) {
                redisTemplate.opsForValue().increment(tryLoginCountMark);

                return Result.error(Result.Code.E_UNKNOWN_ERROR, Result.Msg.E_UNKNOWN_ERROR);
            }
        } catch(Exception e) {
            redisTemplate.opsForValue().increment(tryLoginCountMark);

            throw e;
        }

        //登录成功清除
        redisTemplate.delete(tryLoginCountMark);

        actionLogService.addRequestLog("login", "uid:" + user.getId(), "api", request);

        //生成jwt token
        JwtUser jwtUser = new JwtUser();
        jwtUser.setUid(user.getId());
        jwtUser.setUsername(user.getAccount());
        //jwtUser.setSalt(DateTimeUtils.getLongFormat(user.getRegisterTime()));
        jwtUser.setSalt(user.getSalt());

        //通过认证, 生成签名
        String token = JwtUtils.sign(jwtUser);
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);

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
    public Result logout(HttpServletRequest  request, HttpServletResponse response) {
        // 清除缓存数据
        Cookie cookie = new Cookie("uid", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        HttpSession session = request.getSession();
        session.removeAttribute("uid");
        
        return Result.success(null);
    }

}

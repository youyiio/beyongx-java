package com.beyongx.system.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.beyongx.common.validation.group.Always;
import com.beyongx.common.vo.Result;
import com.beyongx.framework.service.ICodeService;
import com.beyongx.framework.shiro.JwtUser;
import com.beyongx.framework.shiro.JwtUtils;
import com.beyongx.system.entity.SysUser;
import com.beyongx.system.service.ISysActionLogService;
import com.beyongx.system.service.ISysUserService;
import com.beyongx.system.vo.SmsVo;
import com.beyongx.system.vo.SmsVo.Login;
import com.beyongx.system.vo.SmsVo.SendCode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/api/sms")
@Slf4j
public class SmsController {
    
    @Autowired
    private ICodeService codeService;
    @Autowired
    private ISysActionLogService actionLogService;
    @Autowired
    private ISysUserService userService;

    @PostMapping(value = "/sendCode")
    public Result sendCode(@Validated({Always.class, SendCode.class}) @RequestBody SmsVo smsVo) {
        String mobile = smsVo.getMobile();
        String action = smsVo.getAction();

        // 防止短信被刷

        boolean success = codeService.sendCodeByMobile(mobile, action);
        if (!success) {
            return Result.error(Result.Code.ACTION_FAILED, "短信发送失败!");
        }

        return Result.success(null);
    }

    @PostMapping(value = "/login")
    public Result login(@Validated({Always.class, Login.class}) @RequestBody SmsVo smsVo, HttpServletRequest request) {
        String mobile = smsVo.getMobile();
        String code = smsVo.getCode();

        boolean check = codeService.checkCode(ICodeService.TYPE_LOGIN, mobile, code);
        if (!check) {
            return Result.error(Result.Code.E_CODE_INCORRECT, "登录验证码不正确!");
        }

        SysUser user = userService.findByMobile(mobile);
        
        actionLogService.addRequestLog("login", "uid:" + user.getId(), "api", request);

        //消耗验证码
        codeService.consumeCode(ICodeService.TYPE_LOGIN, mobile, code);

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
}

package com.beyongx.framework.service.impl;

import java.util.concurrent.TimeUnit;

import com.beyongx.common.exception.ServiceException;
import com.beyongx.common.vo.Result;
import com.beyongx.framework.service.ICodeService;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class CodeServiceImpl implements ICodeService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean sendCodeByMobile(String mobile, String type) {
        // TODO 发送邮件过程
        String code = RandomStringUtils.randomNumeric(6);
        //hack
        code = "123456";

        // 缓存验证码
        redisTemplate.opsForValue().set(type + ":" + mobile, code, 5 * 60, TimeUnit.SECONDS);

        return true;
    }

    @Override
    public boolean sendCodeByEmail(String mobile, String type) {
        // TODO 短信发送过程
        String code = RandomStringUtils.randomNumeric(6);
        //hack
        code = "123456";

        // 缓存验证码
        redisTemplate.opsForValue().set(type + ":" + mobile, code, 5 * 60, TimeUnit.SECONDS);

        return false;
    }

    @Override
    public boolean checkCode(String type, String username, String code) {
        String cacheCode = (String)redisTemplate.opsForValue().get(type + ":" + username);
        if (cacheCode == null || code == null) {
            throw new ServiceException(Result.Code.E_CODE_EXPIRED, Result.Msg.E_CODE_EXPIRED);
        }
        if (!StringUtils.equals(code, cacheCode)) {
            throw new ServiceException(Result.Code.E_CODE_INCORRECT, Result.Msg.E_CODE_INCORRECT);
        }

        return true;
    }

    @Override
    public boolean consumeCode(String type, String username, String code) {
        String cacheCode = (String)redisTemplate.opsForValue().get(type + ":" + username);
        if (cacheCode == null || !StringUtils.equals(cacheCode, code)) {
            throw new ServiceException(Result.Code.E_CODE_INCORRECT, "验证码不正确或已过期!");
        }

        redisTemplate.delete(type + ":" + username);

        return true;
    }
    
}

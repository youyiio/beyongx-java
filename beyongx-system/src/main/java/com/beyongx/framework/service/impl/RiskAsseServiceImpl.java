package com.beyongx.framework.service.impl;

import java.util.concurrent.TimeUnit;

import com.beyongx.common.exception.ServiceException;
import com.beyongx.common.utils.DateTimeUtils;
import com.beyongx.common.vo.Result;
import com.beyongx.framework.service.IRiskAsseService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RiskAsseServiceImpl implements IRiskAsseService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    @Override
    public boolean checkLogin(String username, String ip, String code) {
        String tryLoginCountMark = username + "_try_login_count";
        Integer tryLoginCount = (Integer)redisTemplate.opsForValue().getAndSet(tryLoginCountMark, 0);
        redisTemplate.opsForValue().increment(tryLoginCountMark);

        redisTemplate.expireAt(tryLoginCountMark, DateTimeUtils.tomorrow());

        if (tryLoginCount >= 3 && tryLoginCount < 5 && StringUtils.isBlank(code)) {
            throw new ServiceException(Result.Code.E_RISK_ASSE_CODE_CHECK, Result.Msg.E_RISK_ASSE_CODE_CHECK);
        }
        if (tryLoginCount > 5) {
            throw new ServiceException(Result.Code.E_USER_STATUS_FREED, Result.Msg.E_USER_STATUS_FREED);
        }
        if (tryLoginCount >= 5) {
            redisTemplate.opsForValue().set(tryLoginCountMark, tryLoginCount + 1);

            throw new ServiceException(Result.Code.E_USER_STATUS_FREED, "登录错误超过5次,账号被临时冻结1天");
        }
        
        return true;
    }

    @Override
    public boolean resetLogin(String username, String ip) {
        String tryLoginCountMark = username + "_try_login_count";
        redisTemplate.delete(tryLoginCountMark);

        return true;
    }

    @Override
    public boolean checkSms(String mobile, String ip) {
        String mobileFrequencyMark = mobile + "_send_code_frequency";
        String ipFrequencyMark = ip + "_send_code_frequency";

        Integer mobileFrequency = (Integer)redisTemplate.opsForValue().getAndSet(mobileFrequencyMark, 0);
        Integer ipFrequency = (Integer)redisTemplate.opsForValue().getAndSet(ipFrequencyMark, 0);
        
        redisTemplate.expireAt(mobileFrequencyMark, DateTimeUtils.tomorrow());
        redisTemplate.expireAt(ipFrequencyMark, DateTimeUtils.tomorrow());

        boolean frequencyLimited = mobileFrequency > 0 || ipFrequency > 0;
        if (frequencyLimited) {
            throw new ServiceException(Result.Code.ACTION_FAILED, "您操作过于频繁，请稍候再试!");
        }

        redisTemplate.opsForValue().set(mobileFrequencyMark, 60, 60, TimeUnit.SECONDS);
        redisTemplate.opsForValue().set(ipFrequencyMark, 20, 20, TimeUnit.SECONDS);

        return true;
    }
    
    @Override
    public boolean resetSms(String mobile, String ip) {
        String mobileFrequencyMark = mobile + "_send_code_frequency";
        String ipFrequencyMark = ip + "_send_code_frequency";

        redisTemplate.delete(mobileFrequencyMark);
        redisTemplate.delete(ipFrequencyMark);

        return true;
    }
}

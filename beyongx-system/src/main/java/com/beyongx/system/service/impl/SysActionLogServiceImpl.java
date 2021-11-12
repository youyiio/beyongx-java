package com.beyongx.system.service.impl;

import com.beyongx.common.utils.IpUtils;
import com.beyongx.common.utils.ParameterUtils;
import com.beyongx.system.entity.SysActionLog;
import com.beyongx.system.mapper.SysActionLogMapper;
import com.beyongx.system.service.ISysActionLogService;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.stereotype.Service;

/**
 * <p>
 * 操作日志表 服务实现类
 * </p>
 *
 * @author youyi.io
 * @since 2021-07-01
 */
@Service
public class SysActionLogServiceImpl extends ServiceImpl<SysActionLogMapper, SysActionLog> implements ISysActionLogService {
    

    @Override
    public void addRequestLog(String action, String username, String module, HttpServletRequest request) {
        SysActionLog actionLog = new SysActionLog();
        actionLog.setAction(action);
        actionLog.setUsername(username);
        actionLog.setModule(module);

        String ip = IpUtils.getIpAddr(request);
        String component = request.getRequestURI();
        String params = ParameterUtils.getParams(request);
        String userAgent = request.getHeader("user-agent");
        actionLog.setComponent(component);
        actionLog.setIp(ip);
        actionLog.setParams(params);
        actionLog.setUserAgent(userAgent);
        actionLog.setResponse("success");
        actionLog.setResponseTime(System.currentTimeMillis());
        actionLog.setCreateTime(new Date());

        baseMapper.insert(actionLog);
    }

    @Override
    public void addInvokeLog(String action, String module, String component, String params, String response,
            Long actionTime, Long responseTime) {
        SysActionLog actionLog = new SysActionLog();
        actionLog.setAction(action);
        actionLog.setUsername(null);
        actionLog.setModule(module);

        actionLog.setComponent(component);
        actionLog.setIp(null);
        actionLog.setParams(params);
        actionLog.setUserAgent(null);
        actionLog.setActionTime(actionTime);
        actionLog.setResponse(response);
        actionLog.setResponseTime(responseTime);
        actionLog.setCreateTime(new Date());
        
        baseMapper.insert(actionLog);
        
    }

    
}

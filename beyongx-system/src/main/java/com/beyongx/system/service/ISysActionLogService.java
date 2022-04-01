package com.beyongx.system.service;

import com.beyongx.system.entity.SysActionLog;

import javax.servlet.http.HttpServletRequest;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 操作日志表 服务类
 * </p>
 *
 * @author youyi.io
 * @since 2021-07-01
 */
public interface ISysActionLogService extends IService<SysActionLog> {

    //客户端请求日志
    void addRequestLog(String action, String username, String module, HttpServletRequest request);

    //调用其他服务或第三方接口结果日志
    void addInvokeLog(String action, String module, String component, String params, String response, Long actionTime, Long responseTime);

}

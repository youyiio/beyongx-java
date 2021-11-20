package com.beyongx.system.controller;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import com.beyongx.common.utils.DateTimeUtils;
import com.beyongx.common.vo.Result;
import com.beyongx.framework.service.IServerService;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;

@RestController
@RequestMapping("/api/server")
@Slf4j
public class ServerController {
    
    @Autowired
    private IServerService serverService;

    @RequiresPermissions("server:status")
    @GetMapping("/status")
    public Result status() {
        //Map<String, Object> serverStatus = serverService.getStatus();

        Map<String, Object> resultMap = new LinkedHashMap<>(8);
        try {
            // 系统信息
            resultMap.put("sys", serverService.getSystemInfo());
            // cpu 信息
            resultMap.put("cpu", serverService.getCpuInfo());
            // 内存信息
            resultMap.put("memory", serverService.getMemoryInfo());
            // 交换区信息
            resultMap.put("swap", serverService.getSwapInfo());
            // 磁盘
            resultMap.put("disk", serverService.getDiskInfo());
            
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        resultMap.put("time", DateTimeUtils.getFormatString(new Date(), "HH:mm:ss"));
        
        return Result.success(resultMap);
    }
}

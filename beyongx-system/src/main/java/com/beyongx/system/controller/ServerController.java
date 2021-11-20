package com.beyongx.system.controller;

import java.util.Map;

import com.beyongx.common.vo.Result;
import com.beyongx.framework.service.IServerService;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/server")
@Slf4j
public class ServerController {
    
    @Autowired
    private IServerService serverService;

    @RequiresPermissions("server:status")
    @GetMapping("/status")
    public Result status() {
        Map<String, Object> serverStatus = serverService.getStatus();

        return Result.success(serverStatus);
    }
}

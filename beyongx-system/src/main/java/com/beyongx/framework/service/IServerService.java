package com.beyongx.framework.service;

import java.util.Map;
public interface IServerService {

    Map<String,Object> getDiskInfo();

    Map<String,Object> getSwapInfo();

    Map<String,Object> getMemoryInfo();

    Map<String,Object> getCpuInfo();

    Map<String,Object> getSystemInfo();
}

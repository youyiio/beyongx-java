package com.beyongx.framework.service.impl;

import java.lang.management.ManagementFactory;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.beyongx.common.utils.FileUtils;
import com.beyongx.common.utils.IpUtils;
import com.beyongx.framework.service.IServerService;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.VirtualMemory;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.util.FormatUtil;
import oshi.util.Util;

@Service
@Slf4j
public class ServerServiceImpl implements IServerService {

    /**
     * win 系统
     */
    public static final String WIN = "win";

    /**
     * mac 系统
     */
    public static final String MAC = "mac";

    private final DecimalFormat df = new DecimalFormat("0.00");
    
    private SystemInfo si = new SystemInfo();
    
    /**
     * 获取磁盘信息
     * @return /
     */
    @Override
    @Cacheable(cacheNames="server")
    public Map<String,Object> getDiskInfo() {
        
        OperatingSystem os = si.getOperatingSystem();

        Map<String,Object> diskInfo = new LinkedHashMap<>();
        FileSystem fileSystem = os.getFileSystem();
        List<OSFileStore> fsArray = fileSystem.getFileStores();
        String osName = System.getProperty("os.name");
        long available = 0, total = 0;
        for (OSFileStore fs : fsArray){
            // windows 需要将所有磁盘分区累加，linux 和 mac 直接累加会出现磁盘重复的问题，待修复
            if(osName.toLowerCase().startsWith(WIN)) {
                available += fs.getUsableSpace();
                total += fs.getTotalSpace();
            } else {
                available = fs.getUsableSpace();
                total = fs.getTotalSpace();
                break;
            }
        }
        long used = total - available;
        diskInfo.put("total", total > 0 ? FileUtils.getSize(total) : "?");
        diskInfo.put("available", FileUtils.getSize(available));
        diskInfo.put("used", FileUtils.getSize(used));
        if(total != 0){
            diskInfo.put("usageRate", df.format(used/(double)total * 100));
        } else {
            diskInfo.put("usageRate", 0);
        }
        return diskInfo;
    }

    /**
     * 获取交换区信息
     * @param memory /
     * @return /
     */
    @Override
    @Cacheable(cacheNames="server")
    public Map<String,Object> getSwapInfo() {
        HardwareAbstractionLayer hal = si.getHardware();
        GlobalMemory memory = hal.getMemory();

        Map<String,Object> swapInfo = new LinkedHashMap<>();
        VirtualMemory virtualMemory = memory.getVirtualMemory();
        long total = virtualMemory.getSwapTotal();
        long used = virtualMemory.getSwapUsed();
        swapInfo.put("total", FormatUtil.formatBytes(total));
        swapInfo.put("used", FormatUtil.formatBytes(used));
        swapInfo.put("available", FormatUtil.formatBytes(total - used));
        if(used == 0){
            swapInfo.put("usageRate", 0);
        } else {
            swapInfo.put("usageRate", df.format(used/(double)total * 100));
        }
        return swapInfo;
    }

    /**
     * 获取内存信息
     * @param memory /
     * @return /
     */
    @Override
    public Map<String,Object> getMemoryInfo() {
        HardwareAbstractionLayer hal = si.getHardware();
        GlobalMemory memory = hal.getMemory();

        Map<String,Object> memoryInfo = new LinkedHashMap<>();
        memoryInfo.put("total", FormatUtil.formatBytes(memory.getTotal()));
        memoryInfo.put("available", FormatUtil.formatBytes(memory.getAvailable()));
        memoryInfo.put("used", FormatUtil.formatBytes(memory.getTotal() - memory.getAvailable()));
        memoryInfo.put("usageRate", df.format((memory.getTotal() - memory.getAvailable())/(double)memory.getTotal() * 100));
        return memoryInfo;
    }

    /**
     * 获取Cpu相关信息
     * @param processor /
     * @return /
     */
    @Override
    public Map<String,Object> getCpuInfo() {
        HardwareAbstractionLayer hal = si.getHardware();
        CentralProcessor processor = hal.getProcessor();

        Map<String,Object> cpuInfo = new LinkedHashMap<>();
        cpuInfo.put("name", processor.getProcessorIdentifier().getName());
        cpuInfo.put("package", processor.getPhysicalPackageCount() + "个物理CPU");
        cpuInfo.put("core", processor.getPhysicalProcessorCount() + "个物理核心");
        cpuInfo.put("coreNumber", processor.getPhysicalProcessorCount());
        cpuInfo.put("logic", processor.getLogicalProcessorCount() + "个逻辑CPU");
        // CPU信息
        long[] prevTicks = processor.getSystemCpuLoadTicks();

        // 等待1秒...
        Util.sleep(1000);

        long[] ticks = processor.getSystemCpuLoadTicks();
        long user = ticks[CentralProcessor.TickType.USER.getIndex()] - prevTicks[CentralProcessor.TickType.USER.getIndex()];
        long nice = ticks[CentralProcessor.TickType.NICE.getIndex()] - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
        long sys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
        long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()] - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
        long iowait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()] - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
        long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()] - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
        long softirq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
        long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()] - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
        long totalCpu = user + nice + sys + idle + iowait + irq + softirq + steal;
        cpuInfo.put("used", df.format(100d * user / totalCpu + 100d * sys / totalCpu));
        cpuInfo.put("idle", df.format(100d * idle / totalCpu));
        return cpuInfo;
    }

    /**
     * 获取系统相关信息,系统、运行天数、系统IP
     * @param os /
     * @return /
     */
    @Override
    public Map<String,Object> getSystemInfo() {
        OperatingSystem os = si.getOperatingSystem();

        Map<String,Object> systemInfo = new LinkedHashMap<>();
        // jvm 运行时间
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        Date date = new Date(time);
        // 计算项目运行时间
        String formatBetween = df.format((new Date().getTime() - date.getTime()) / 24 * 60 * 60 * 1000);
        // 系统信息
        systemInfo.put("os", os.toString());
        systemInfo.put("day", formatBetween);
        systemInfo.put("ip", IpUtils.getLocalIP());
        return systemInfo;
    }
}

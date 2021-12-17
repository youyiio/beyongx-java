package com.beyongx.system.service;

import com.beyongx.system.entity.SysConfig;
import com.beyongx.system.vo.ConfigVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 系统字典表 服务类
 * </p>
 *
 * @author youyi.io
 * @since 2021-11-05
 */
public interface ISysConfigService extends IService<SysConfig> {

    SysConfig getByKey(String key);

    SysConfig getByGroupAndKey(String group, String key);

    SysConfig createConfig(ConfigVo configVo);

    SysConfig editConfig(ConfigVo configVo);
}

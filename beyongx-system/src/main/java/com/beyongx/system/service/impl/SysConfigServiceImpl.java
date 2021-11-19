package com.beyongx.system.service.impl;

import com.beyongx.system.entity.SysConfig;
import com.beyongx.system.mapper.SysConfigMapper;
import com.beyongx.system.service.ISysConfigService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统字典表 服务实现类
 * </p>
 *
 * @author youyi.io
 * @since 2021-07-01
 */
@Service
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig> implements ISysConfigService {

    @Override
    public SysConfig getByKey(String key) {
        QueryWrapper<SysConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("`key`", key);

        SysConfig sysConfig = baseMapper.selectOne(queryWrapper);
        return sysConfig;
    }

    @Override
    public SysConfig getByGroupAndKey(String group, String key) {
        QueryWrapper<SysConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("`group`", group);
        queryWrapper.eq("`key`", key);

        SysConfig sysConfig = baseMapper.selectOne(queryWrapper);
        return sysConfig;
    }

}

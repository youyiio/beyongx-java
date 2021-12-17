package com.beyongx.system.service.impl;

import com.beyongx.common.exception.ServiceException;
import com.beyongx.common.vo.Result;
import com.beyongx.framework.utils.BeanPropertyUtils;
import com.beyongx.system.entity.SysConfig;
import com.beyongx.system.entity.meta.ConfigMeta;
import com.beyongx.system.mapper.SysConfigMapper;
import com.beyongx.system.service.ISysConfigService;
import com.beyongx.system.vo.ConfigVo;

import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
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

    @Override
    public SysConfig createConfig(ConfigVo configVo) {
        QueryWrapper<SysConfig> wrapper = new QueryWrapper<>();
        wrapper.eq("`group`", configVo.getGroup()).eq("`key`", configVo.getKey());
        if (baseMapper.selectOne(wrapper) != null) {
            throw new ServiceException(Result.Code.E_CONFIG_GROUP_KEY_UNIQ, Result.Msg.E_CONFIG_GROUP_KEY_UNIQ);
        }

        SysConfig config = new SysConfig();
        try {
            BeanUtils.copyProperties(configVo, config);;
        } catch (Exception e) {            
            log.error("beanutils copyProperites error:", e);
        }

        if (config.getStatus() == null) {
            config.setStatus(ConfigMeta.Status.ONLINE.getCode());
        }
        if (config.getSort() == null) {
            config.setSort(0);
        }

        Date currentTime = new Date();
        config.setUpdateTime(currentTime);
        config.setCreateTime(currentTime);

        baseMapper.insert(config);

        return config;
    }

    @Override
    public SysConfig editConfig(ConfigVo configVo) {
        SysConfig config = baseMapper.selectById((Integer)configVo.getId());
        if (config == null) {
            throw new ServiceException(Result.Code.E_CONFIG_NOT_FOUND, Result.Msg.E_CONFIG_NOT_FOUND);
        }

        if (!StringUtils.equals(config.getGroup(), configVo.getGroup()) || !StringUtils.equals(config.getKey(), configVo.getKey())) {
            QueryWrapper<SysConfig> wrapper = new QueryWrapper<>();
            wrapper.eq("`group`", configVo.getGroup()).eq("`key`", configVo.getKey());
            wrapper.ne("id", config.getId());            
            if (baseMapper.selectOne(wrapper) != null) {
                throw new ServiceException(Result.Code.E_CONFIG_GROUP_KEY_UNIQ, Result.Msg.E_CONFIG_GROUP_KEY_UNIQ);
            }
        }
        

        try {
            String[] ignoreProperties = BeanPropertyUtils.getNullPropertyNames(configVo);
            BeanUtils.copyProperties(configVo, config, ignoreProperties);
        } catch (Exception e) {            
            log.error("beanutils copyProperites error:", e);
        }

        Date currentTime = new Date();
        config.setUpdateTime(currentTime);

        baseMapper.updateById(config);

        return config;
    }

}

package com.beyongx.system.service.impl;

import com.beyongx.system.entity.SysUserMeta;
import com.beyongx.system.entity.meta.UserMeta;
import com.beyongx.system.entity.meta.UserMeta.Mode;
import com.beyongx.system.mapper.SysUserMetaMapper;
import com.beyongx.system.service.ISysUserMetaService;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户元数据表 服务实现类
 * </p>
 *
 * @author youyi.io
 * @since 2021-07-01
 */
@Service
public class SysUserMetaServiceImpl extends ServiceImpl<SysUserMetaMapper, SysUserMeta> implements ISysUserMetaService {

    @Override
    public SysUserMeta meta(Integer uid, String metaKey) {
        QueryWrapper<SysUserMeta> wrapper = new QueryWrapper<>();
        wrapper.eq("target_id", uid);
        wrapper.eq("meta_key", metaKey);

        SysUserMeta userMeta = baseMapper.selectOne(wrapper);

        return userMeta;
    }

    @Override
    public List<SysUserMeta> metas(Integer uid, String metaKey) {
        QueryWrapper<SysUserMeta> wrapper = new QueryWrapper<>();
        wrapper.eq("target_id", uid);
        wrapper.eq("meta_key", metaKey);

        List<SysUserMeta> userMetaList = baseMapper.selectList(wrapper);

        return userMetaList;
    }

    @Override
    public boolean meta(Integer uid, String metaKey, String metaValue) {
        return this.meta(uid, metaKey, metaValue, Mode.SINGLE);
    }

    @Override
    public boolean meta(Integer uid, String metaKey, String metaValue, Mode mode) {
        if (mode == UserMeta.Mode.SINGLE) {
            QueryWrapper<SysUserMeta> wrapper = new QueryWrapper<>();
            wrapper.eq("target_id", uid);
            wrapper.eq("meta_key", metaKey);
            SysUserMeta userMeta = baseMapper.selectOne(wrapper);
            if (userMeta == null) {
                userMeta = new SysUserMeta();
                userMeta.setTargetId(uid);
                userMeta.setMetaKey(metaKey); 
                userMeta.setMetaValue(metaValue);
                userMeta.setUpdateTime(new Date()); 
                userMeta.setCreateTime(new Date());
                baseMapper.insert(userMeta);    
            } else {
                userMeta.setMetaValue(metaValue);
                userMeta.setUpdateTime(new Date());
                baseMapper.updateById(userMeta);
            }
            
        } else if (mode == UserMeta.Mode.MULTIPLE) {
            QueryWrapper<SysUserMeta> wrapper = new QueryWrapper<>();
            wrapper.eq("target_id", uid);
            wrapper.eq("meta_key", metaKey);
            wrapper.eq("meta_value", metaValue);
    
            SysUserMeta userMeta = baseMapper.selectOne(wrapper);
            if (userMeta == null) {
                userMeta = new SysUserMeta();
                userMeta.setTargetId(uid);
                userMeta.setMetaKey(metaKey); 
                userMeta.setMetaValue(metaValue);
                userMeta.setUpdateTime(new Date()); 
                userMeta.setCreateTime(new Date());
                baseMapper.insert(userMeta);
            }
        }
        

        return true;
    }

    @Override
    public boolean remove(Integer uid, String metaKey) {
        QueryWrapper<SysUserMeta> wrapper = new QueryWrapper<>();
        wrapper.eq("target_id", uid);
        wrapper.eq("meta_key", metaKey);

        baseMapper.delete(wrapper);

        return true;
    }

    @Override
    public boolean remove(Integer uid, String metaKey, String metaValue) {
        QueryWrapper<SysUserMeta> wrapper = new QueryWrapper<>();
        wrapper.eq("target_id", uid);
        wrapper.eq("meta_key", metaKey);
        wrapper.eq("meta_value", metaValue);

        baseMapper.delete(wrapper);

        return true;
    }

}

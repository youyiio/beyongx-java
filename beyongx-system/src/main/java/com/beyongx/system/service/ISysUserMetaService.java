package com.beyongx.system.service;

import com.beyongx.system.entity.SysUserMeta;
import com.beyongx.system.entity.meta.UserMeta;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户元数据表 服务类
 * </p>
 *
 * @author youyi.io
 * @since 2021-07-01
 */
public interface ISysUserMetaService extends IService<SysUserMeta> {

    SysUserMeta meta(Integer uid, String metaKey);
    
    List<SysUserMeta> metas(Integer uid, String metaKey);
    
    //单值设置
    boolean meta(Integer uid, String metaKey, String metaValue);

    boolean meta(Integer uid, String metaKey, String metaValue, UserMeta.Mode mode);

    boolean remove(Integer uid, String metaKey);

    boolean remove(Integer uid, String metaKey, String metaValue);
}

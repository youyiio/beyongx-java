package com.beyongx.system.mapper;

import com.beyongx.system.entity.CmsAd;

import java.util.Map;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * <p>
 * 广告表 Mapper 接口
 * </p>
 *
 * @author youyi.io
 * @since 2021-11-05
 */
public interface CmsAdMapper extends BaseMapper<CmsAd> {

    IPage<CmsAd> selectBySlotId(IPage<CmsAd> page, Integer slotId, Map<String, Object> params);
    
}

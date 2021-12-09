package com.beyongx.system.service;

import com.beyongx.system.entity.CmsAd;
import com.beyongx.system.vo.AdVo;

import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 广告表 服务类
 * </p>
 *
 * @author youyi.io
 * @since 2021-11-05
 */
public interface ICmsAdService extends IService<CmsAd> {

    IPage<AdVo> listBySlotId(IPage<CmsAd> page, Integer slotId, Map<String, Object> params);

    AdVo createAd(AdVo adVo);

    AdVo editAd(AdVo adVo);

    CmsAd setStatus(Integer id, Integer status);

    boolean removeAd(Integer id);
}

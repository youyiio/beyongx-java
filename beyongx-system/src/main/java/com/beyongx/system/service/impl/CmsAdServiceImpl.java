package com.beyongx.system.service.impl;

import com.beyongx.common.exception.ServiceException;
import com.beyongx.common.vo.Result;
import com.beyongx.system.entity.CmsAd;
import com.beyongx.system.entity.CmsAdServing;
import com.beyongx.system.entity.CmsAdSlot;
import com.beyongx.system.entity.meta.AdServingMeta;
import com.beyongx.system.mapper.CmsAdMapper;
import com.beyongx.system.mapper.CmsAdServingMapper;
import com.beyongx.system.mapper.CmsAdSlotMapper;
import com.beyongx.system.service.ICmsAdService;
import com.beyongx.system.vo.AdServingVo;
import com.beyongx.system.vo.AdVo;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.BeanUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 广告表 服务实现类
 * </p>
 *
 * @author youyi.io
 * @since 2021-11-05
 */
@Service
public class CmsAdServiceImpl extends ServiceImpl<CmsAdMapper, CmsAd> implements ICmsAdService {

    @Autowired
    private CmsAdServingMapper adServingMapper;
    @Autowired
    private CmsAdSlotMapper adSlotMapper;

    @Override
    public IPage<AdVo> listBySlotId(IPage<CmsAd> page, Integer slotId, Map<String, Object> params) {
        IPage<AdVo> pageList = baseMapper.selectBySlotId(page, slotId, params);
        for (AdVo adVo : pageList.getRecords()) {    
            QueryWrapper<CmsAdServing> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("ad_id", adVo.getId());
            //queryWrapper.eq("status", AdServingMeta.Status.ONLINE.getCode());
            List<CmsAdServing> servings = adServingMapper.selectList(queryWrapper);    
            List<Map<String, Object>> servingMaps = BeanUtils.beansToMaps(servings);    
            List<AdServingVo> servingVos = BeanUtils.mapsToBeans(servingMaps, AdServingVo.class);
            for (AdServingVo adServingVo : servingVos) {
                CmsAdSlot slot = adSlotMapper.selectById(adServingVo.getSlotId());
                adServingVo.setSlot(slot);
            }

            adVo.setServings(servingVos);
        }

        return pageList;
    }

    @Override
    public AdVo createAd(AdVo adVo) {
        CmsAd ad = new CmsAd();
        try {
            org.apache.commons.beanutils.BeanUtils.copyProperties(ad, adVo);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error(e.getMessage(), e);
        }

        Date currentTime = new Date();
        ad.setCreateTime(currentTime);
        boolean success = this.save(ad);
        if (!success) {
            throw new ServiceException(Result.Code.E_DB_ERROR, "新增广告失败!");
        }

        List<Integer> slotIds = adVo.getSlotIds();
        for (Integer slotId : slotIds) {
            CmsAdServing adServing = new CmsAdServing();
            adServing.setAdId(ad.getId());
            adServing.setSlotId(slotId);
            adServing.setSort(adVo.getSort());
            adServing.setStatus(AdServingMeta.Status.ONLINE.getCode());
            adServing.setStartTime(null);
            adServing.setEndTime(null);
            adServing.setUpdateTime(currentTime);
            adServing.setCreateTime(currentTime);

            adServingMapper.insert(adServing);
        }

        //封装放回数据

        try {
            org.apache.commons.beanutils.BeanUtils.copyProperties(adVo, ad);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error(e.getMessage(), e);
        }

        QueryWrapper<CmsAdServing> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ad_id", adVo.getId());
        //queryWrapper.eq("status", AdServingMeta.Status.ONLINE.getCode());
        List<CmsAdServing> servings = adServingMapper.selectList(queryWrapper);    
        List<Map<String, Object>> servingMaps = BeanUtils.beansToMaps(servings);      
        List<AdServingVo> servingVos = BeanUtils.mapsToBeans(servingMaps, AdServingVo.class);
        for (AdServingVo adServingVo : servingVos) {
            CmsAdSlot slot = adSlotMapper.selectById(adServingVo.getSlotId());
            adServingVo.setSlot(slot);
        }
        adVo.setServings(servingVos);

        return adVo;
    }

    @Override
    public AdVo editAd(AdVo adVo) {
         CmsAd ad = new CmsAd();
        try {
            org.apache.commons.beanutils.BeanUtils.copyProperties(ad, adVo);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error(e.getMessage(), e);
        }

        Date currentTime = new Date();
        boolean success = this.saveOrUpdate(ad);
        if (!success) {
            throw new ServiceException(Result.Code.E_DB_ERROR, "更新广告失败!");
        }

        //清除关联表
        QueryWrapper<CmsAdServing> wrapper = new QueryWrapper<>();
        wrapper.eq("ad_id", ad.getId());
        adServingMapper.delete(wrapper);

        List<Integer> slotIds = adVo.getSlotIds();
        for (Integer slotId : slotIds) {
            CmsAdServing adServing = new CmsAdServing();
            adServing.setAdId(ad.getId());
            adServing.setSlotId(slotId);
            adServing.setSort(adVo.getSort());
            adServing.setStatus(AdServingMeta.Status.ONLINE.getCode());
            adServing.setStartTime(null);
            adServing.setEndTime(null);
            adServing.setUpdateTime(currentTime);
            adServing.setCreateTime(currentTime);

            adServingMapper.insert(adServing);
        }
        
        //封装放回数据
        try {
            org.apache.commons.beanutils.BeanUtils.copyProperties(adVo, ad);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error(e.getMessage(), e);
        }

        QueryWrapper<CmsAdServing> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ad_id", adVo.getId());
        //queryWrapper.eq("status", AdServingMeta.Status.ONLINE.getCode());
        List<CmsAdServing> servings = adServingMapper.selectList(queryWrapper);    
        List<Map<String, Object>> servingMaps = BeanUtils.beansToMaps(servings);    
        List<AdServingVo> servingVos = BeanUtils.mapsToBeans(servingMaps, AdServingVo.class);
        for (AdServingVo adServingVo : servingVos) {
            CmsAdSlot slot = adSlotMapper.selectById(adServingVo.getSlotId());
            adServingVo.setSlot(slot);
        }
        adVo.setServings(servingVos);

        return adVo;
    }

    @Override
    public CmsAd setStatus(Integer id, Integer status) {
        if (status != AdServingMeta.Status.OFFLINE.getCode() && status != AdServingMeta.Status.ONLINE.getCode()) {
            throw new ServiceException(Result.Code.E_DATA_VALIDATE_ERROR, "广告status值不合法!");
        }

        CmsAd ad = baseMapper.selectById(id);
        if (ad == null) {
            throw new ServiceException(Result.Code.E_DATA_NOT_FOUND, "广告不存在!");
        }

        QueryWrapper<CmsAdServing> wrapper = new QueryWrapper<>();
        wrapper.eq("ad_id", id);
        List<CmsAdServing> adServings = adServingMapper.selectList(wrapper);
        for (CmsAdServing cmsAdServing : adServings) {
            cmsAdServing.setStatus(status);
            adServingMapper.updateById(cmsAdServing);
        }

        return ad;
    }

    @Override
    public boolean removeAd(Integer id) {
        int rownums = baseMapper.deleteById(id);
        if (rownums <= 0) {
            return false;
        }
        
        QueryWrapper<CmsAdServing> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ad_id", id);
        adServingMapper.delete(queryWrapper);        

        return true;
    }

}

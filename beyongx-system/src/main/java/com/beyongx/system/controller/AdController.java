package com.beyongx.system.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.druid.sql.dialect.ads.parser.AdsCreateTableParser;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.beyongx.common.validation.group.Always;
import com.beyongx.common.vo.Result;
import com.beyongx.framework.vo.PageVo;
import com.beyongx.system.entity.CmsAd;
import com.beyongx.system.entity.CmsAdSlot;
import com.beyongx.system.service.ICmsAdService;
import com.beyongx.system.service.ICmsAdSlotService;
import com.beyongx.system.vo.AdVo;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/ad")
@Slf4j
public class AdController {
    @Autowired
    private ICmsAdService adService;
    @Autowired
    private ICmsAdSlotService adSlotService;

    @RequiresPermissions("ad:list")
    @RequestMapping(value="/list", method = {RequestMethod.GET, RequestMethod.POST})
    public Result list(@Validated @RequestBody PageVo pageVo) {
        QueryWrapper<CmsAd> queryWrapper = new QueryWrapper<>();
        
        //条件过滤
        Map<String, Object> filters = new HashMap<>();
        filters.putAll(pageVo.getFilters());
        
        //排序
        Map<String, String> orders = pageVo.getOrders();
        if (orders.size() == 0) {
            queryWrapper.orderByDesc("sort").orderByDesc("id");
        } else {
            for (String key : orders.keySet()) {
                String val = orders.get(key);
                Boolean isAsc = val.equalsIgnoreCase("asc");
                queryWrapper.orderBy(true, isAsc, key);
            }            
        }

        IPage<CmsAd> page = new Page<>(pageVo.getPage(), pageVo.getSize());
        IPage<CmsAd> pageList = new Page<>(pageVo.getPage(), pageVo.getSize());

        if (filters.containsKey("slotId")) {
            Integer slotId = (Integer)filters.remove("slotId");
            
            Map<String, Object> params = pageVo.getFilters();
            pageList = adService.listBySlotId(page, slotId, params);

        } else {
            pageList = adService.page(page, queryWrapper);
            if (CollectionUtils.isEmpty(pageList.getRecords())) {
                return Result.success(pageList);
            }
        }
        

        return Result.success(pageList);
    }

    @RequiresPermissions("ad:slots")
    @GetMapping("/slots")
    public Result slots() {

        List<CmsAdSlot> slotList = adSlotService.list();

        return Result.success(slotList);
    }

    @RequiresPermissions("ad:create")
    @PostMapping("/create")
    public Result create(@Validated({Always.class}) @RequestBody AdVo adVo) {
        AdVo result = adService.createAd(adVo);

        return Result.success(result);
    }

    @RequiresPermissions("ad:edit")
    @PostMapping("/edit")
    public Result edit(@Validated({Always.class}) @RequestBody AdVo adVo) {
        AdVo result = adService.editAd(adVo);

        return Result.success(result);
    }

    @RequiresPermissions("ad:setStatus")
    @PostMapping("/setStatus")
    public Result setStatus(@Validated({Always.class}) @RequestBody AdVo adVo) {
        CmsAd ad = adService.setStatus(adVo.getId(), adVo.getStatus());

        return Result.success(null);
    }

    @RequiresPermissions("ad:delete")
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable(value="id") Integer id) {
        return Result.success(null);
    }
}
